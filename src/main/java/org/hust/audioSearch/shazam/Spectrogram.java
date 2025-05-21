package org.hust.audioSearch.shazam;
import org.jtransforms.fft.DoubleFFT_1D;
import java.util.ArrayList;
import java.util.Arrays;
import static org.hust.audioSearch.shazam.FFT.getMagnitudeSpectrum;

public class Spectrogram {
    public static double[][] getSpectrogram(double[] inputSignal, int windowSize, int hopSize) {
        int windowNum = (int) Math.ceil((double)(inputSignal.length - windowSize) / hopSize) + 1;
        int paddedLength = hopSize * (windowNum - 1) + windowSize;
        double[] paddedInputSignal = Arrays.copyOf(inputSignal, paddedLength);
        DoubleFFT_1D fft = new DoubleFFT_1D(windowSize);
        double[][] spectrogram = new double[windowNum][windowSize / 2];
        double[] hammingWindow = new double[windowSize];
        for (int i = 0; i < windowSize; i++) {
            hammingWindow[i] = 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (windowSize - 1));
        }
        for (int i = 0; i < windowNum; i++) {
            int start = i * hopSize;
            double[] tmp = Arrays.copyOfRange(paddedInputSignal, start, start + windowSize);
            for (int j = 0; j < windowSize; j++) {
                tmp[j] *= hammingWindow[j];
            }
            fft.realForward(tmp);
            spectrogram[i] = getMagnitudeSpectrum(tmp);
        }
        return spectrogram;
    }

    public static Peak[] extractPeak(double[][] spectrogram, double coeff){
        short[] bands = new short[]{0,10,20, 40, 80, 160, 511};
        ArrayList<Peak> peaks = new ArrayList<>();
        for (int frameIndex = 0; frameIndex < spectrogram.length; frameIndex++) {
            double[] maxMags = new double[bands.length-1];
            short[] maxBinIndices = new short[bands.length-1];
            int[] maxTimestamps = new int[bands.length-1];
            double freqSum =0.0;
            for (int i = 0; i < bands.length - 1 ; i++) {
                double maxFrequency = 0.0;
                short maxBinIndex = 0;
                for (short j = bands[i]; j < bands[i+1]; j++) {
                    if (spectrogram[frameIndex][j]>maxFrequency){
                        maxFrequency = spectrogram[frameIndex][j];
                        maxBinIndex = j;
                    }
                }
                maxMags[i] = maxFrequency;
                maxBinIndices[i] = maxBinIndex;
                maxTimestamps[i]=frameIndex;
                freqSum += maxFrequency;
            }
            double averageFreq = freqSum/(bands.length-1) * coeff;
            for (int i = 0; i < maxMags.length; i++) {
                if (maxMags[i]> averageFreq) {
                    Peak newPeak = new Peak(maxBinIndices[i], maxTimestamps[i]);
                    peaks.add(newPeak);
                }
            }
        }
        return peaks.toArray(new Peak[peaks.size()]);
    }
}
