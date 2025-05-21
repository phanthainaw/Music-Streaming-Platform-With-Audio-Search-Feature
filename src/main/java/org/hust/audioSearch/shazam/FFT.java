package org.hust.audioSearch.shazam;

import org.jtransforms.fft.DoubleFFT_1D;
import java.util.Arrays;

import static org.hust.audioSearch.shazam.PlotUtils.plotDoubleArray;

public class FFT {
    public static double[] createSincKernel(int length, double cutOffFreq) {
        double[] sincKernel = new double[length];
        for (int i = 0; i < length; i++) {
            int offSet = (length - 1) / 2;
            int time = i - offSet;
            double sinc = (time == 0) ? 2 * cutOffFreq : Math.sin(2 * Math.PI * cutOffFreq * time) / (time * Math.PI);
            double window = 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (length - 1));
            sincKernel[i] = sinc * window;
        }
        return sincKernel;
    }

    public static double[] multiplyComplexSpectra(double[] a, double[] b){
        int size = a.length;
        double[] result = new double[size];
        result[0] = a[0] * b[0];
        result[1] = a[1] * b[1];

        for (int i = 1; i < size/2; i ++){
            double real = a[2*i]*b[2*i] - a[2*i+1] * b[2*i+1]  ;
            double imaginary = a[2*i] * b[2*i+1] + a[2*i+1] * b[2*i] ;
            result[2*i] = real;
            result[2*i+1] = imaginary;
        }
        return result;
    }

    public static double[] getMagnitudeSpectrum(double[] fft){
        int size = fft.length/2;
        double[] magnitudeSpectrum = new double[size];
        magnitudeSpectrum[0] = Math.abs(fft[0]); // DC
        magnitudeSpectrum[1] = Math.abs(fft[1]);
        for (int i = 1; i < size; i++) {
            double re = fft[2 * i];
            double im = fft[2 * i + 1];
            magnitudeSpectrum[i] = Math.sqrt(re * re + im * im);
        }
        return magnitudeSpectrum;
    }

    public static double[] lowPassFilter(double[] inputSignal, double cutOffFrequency, double sampleRate) {
        double rc = 1.0 / (2 * Math.PI * cutOffFrequency);
        double dt = 1.0 / sampleRate;
        double alpha = dt / (rc + dt);

        double[] filteredSignal = new double[inputSignal.length];
        filteredSignal[0] = inputSignal[0];  // Initialize first sample directly
        for (int i = 1; i < inputSignal.length; i++) {
            filteredSignal[i] = filteredSignal[i - 1] + alpha * (inputSignal[i] - filteredSignal[i - 1]);
        }
        return filteredSignal;
    }

    public static double[]SincFilter(double[] inputSignal, double cutOffFrequency, double sampleRate){
        int lengthSignal = inputSignal.length;
        System.out.println(lengthSignal);
        DoubleFFT_1D fft = new DoubleFFT_1D(lengthSignal);
        fft.realForward(inputSignal);
        double[] sincKernel = createSincKernel(512, cutOffFrequency / sampleRate);
        sincKernel = Arrays.copyOf(sincKernel, lengthSignal);
        fft.realForward(sincKernel);
        double[] filteredAudioFFT = multiplyComplexSpectra(inputSignal, sincKernel);
        fft.realInverse(filteredAudioFFT, true);
        System.gc();
        return filteredAudioFFT;
    }

    public static double[] highPassFilter(double[] inputSignal, double cutOffFrequency, double sampleRate) {
        double rc = 1.0 / (2 * Math.PI * cutOffFrequency);
        double dt = 1.0 / sampleRate;
        double alpha = rc / (rc + dt);

        double[] filteredSignal = new double[inputSignal.length];
        filteredSignal[0] = inputSignal[0];  // Initialize first sample

        for (int i = 1; i < inputSignal.length; i++) {
            filteredSignal[i] = alpha * (filteredSignal[i - 1] + inputSignal[i] - inputSignal[i - 1]);
        }

        return filteredSignal;
    }

}


