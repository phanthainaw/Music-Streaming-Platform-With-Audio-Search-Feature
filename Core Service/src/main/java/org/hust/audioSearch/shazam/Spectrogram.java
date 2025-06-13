package org.hust.audioSearch.shazam;

import org.jtransforms.fft.DoubleFFT_1D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hust.audioSearch.org.Utils.standardDeviation;
import static org.hust.audioSearch.shazam.FFT.getMagnitudeSpectrum;

public class Spectrogram {
    public static double[][] getSpectrogram(double[] inputSignal, int windowSize, int hopSize) {
        int windowNum = (int) Math.ceil((double) (inputSignal.length - windowSize) / hopSize) + 1;
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

//    public static Peak[] extractPeak(double[][] spectrogram, double coeff, double audioDuration){
//        double binDuration =  audioDuration/spectrogram.length;
//        short[] bands = new short[]{0,10,20, 40, 80, 160, 512};
//        ArrayList<Peak> peaks = new ArrayList<>();
//
//        class Maxima {
//            double magnitude;
//            short binIndex;
//            double timeStamp;
//            public Maxima(double magnitude, short binIndex, double timeStamp){
//                this.magnitude= magnitude;
//                this.binIndex = binIndex;
//                this.timeStamp = timeStamp;
//            }
//        }
//        ArrayList<Maxima> maximas = new ArrayList<>();
//        for (int frameIndex = 0; frameIndex < spectrogram.length; frameIndex++) {
//            for (int i = 0; i < bands.length - 1 ; i++) {
//                double maxFrequency = 0.0;
//                short maxBinIndex = 0;
//                for (short j = bands[i]; j < bands[i+1]; j++) {
//                    if (spectrogram[frameIndex][j]>maxFrequency){
//                        maxFrequency = spectrogram[frameIndex][j];
//                        maxBinIndex = j;
//                    }
//                }
//                maximas.add(new Maxima(maxFrequency,maxBinIndex,frameIndex * binDuration + maxBinIndex * binDuration / spectrogram[0].length));
//            }
//        }
////        plotDoubleArray(Arrays.stream(maximas.toArray(new Maxima[0])).mapToDouble(value -> value.magnitude).toArray(), "peaks magnitude");
//        double magMean = Arrays.stream(maximas.toArray(new Maxima[0])).mapToDouble(value -> value.magnitude).average().orElse(0.0);
//        double magStd = standardDeviation(Arrays.stream(maximas.toArray(new Maxima[0])).mapToDouble(value -> value.magnitude).toArray());
//        double threshold = magMean + coeff * magStd;
//        for (Maxima maxima: maximas){
//            if(maxima.magnitude > threshold) peaks.add(new Peak(maxima.binIndex, maxima.timeStamp));
//        }
//        return peaks.toArray(new Peak[0]);
//    }
    public static Peak[] extractPeak(double[][] spectrogram, double coeff, double audioDuration) {
        short[] bands = new short[]{0, 10, 20, 40, 80, 160, 511};
        ArrayList<Peak> peaks = new ArrayList<>();
        for (int frameIndex = 0; frameIndex < spectrogram.length; frameIndex++) {
            double[] maxMags = new double[bands.length - 1];
            short[] maxBinIndices = new short[bands.length - 1];
            double[] maxTimestamps = new double[bands.length - 1];
            double freqSum = 0.0;
            for (int i = 0; i < bands.length - 1; i++) {
                double maxMagnitude = 0.0;
                short maxBinIndex = 0;
                for (short j = bands[i]; j < bands[i + 1]; j++) {
                    if (spectrogram[frameIndex][j] > maxMagnitude) {
                        maxMagnitude = spectrogram[frameIndex][j];
                        maxBinIndex = j;
                    }
                }
                maxMags[i] = maxMagnitude;
                maxBinIndices[i] = maxBinIndex;
                maxTimestamps[i] = frameIndex;
                freqSum += maxMagnitude;
            }
            double thresholdFreq = freqSum / (bands.length - 1) + standardDeviation(maxMags)*coeff;
            for (int i = 0; i < maxMags.length; i++) {
                if (maxMags[i] > thresholdFreq) {
                    Peak newPeak = new Peak(maxBinIndices[i], maxTimestamps[i]);
                    peaks.add(newPeak);
                }
            }
        }
        return peaks.toArray(new Peak[0]);
    }

    public static Peak[] extractPeak(double[][] spectrogram, int filterSize, double coeff){
        ArrayList<Peak> peaks = new ArrayList<>();
        int filterEdgeSize = (filterSize-1)/2;
        for (int x = 0; x < spectrogram.length ; x++) {
            for (short y = 0; y < spectrogram[0].length; y++) {
                double localMaximum=0.0;
                for (int innerX = Math.max(0,x-filterEdgeSize); innerX <= Math.min(x + filterEdgeSize, spectrogram.length-1) ; innerX++) {
                    for (int innerY = Math.max(0, y-filterEdgeSize) ; innerY <= Math.min(y +filterEdgeSize, spectrogram[0].length-1) ; innerY++){
                        if (spectrogram[innerX][innerY]> localMaximum){
                            localMaximum=spectrogram[innerX][innerY];
                        }
                    }
                }
                if (spectrogram[x][y]==localMaximum) peaks.add(new Peak(y,x));
            }
        }

        double mean = peaks.stream()
                .mapToDouble(p -> spectrogram[(int) p.time][p.frequency])
                .average()
                .orElse(0.0);
//        double std = standardDeviation(peaks.stream().mapToDouble(p -> spectrogram[(int) p.time][p.frequency]).toArray());

        List<Peak> filteredPeaks = peaks.stream()
                .filter(p -> spectrogram[(int) p.time][p.frequency] > mean*coeff)
                .toList();

        return filteredPeaks.toArray(new Peak[0]);
    }
}
