const FFT = require('fft.js');
const { getMagnitudeSpectrum } = require('./Filter')
const { Peak } = require('../model/Peak')

function getSpectrogram(inputSignal, windowSize, hopSize) {
    windowSize = Math.pow(2, Math.ceil(Math.log(windowSize) / Math.log(2)));
    const windowNum = Math.ceil((inputSignal.length - windowSize) / hopSize) + 1;
    const paddedLength = hopSize * (windowNum - 1) + windowSize;
    const paddedInputSignal = [...inputSignal, ...new Array(paddedLength - inputSignal.length).fill(0)];

    const fft = new FFT(windowSize);
    const spectrogram = new Array(windowNum);

    // Generate Hamming window
    const hammingWindow = new Array(windowSize);
    for (let i = 0; i < windowSize; i++) {
        hammingWindow[i] = 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (windowSize - 1));
    }

    // Generate spectrogram
    for (let i = 0; i < windowNum; i++) {
        const start = i * hopSize;
        let tmp = paddedInputSignal.slice(start, start + windowSize);

        // Apply Hamming window
        for (let j = 0; j < windowSize; j++) {
            tmp[j] *= hammingWindow[j];
        }

        windowSpectrum = fft.createComplexArray()
        // Perform FFT
        fft.realTransform(windowSpectrum,tmp);

        // Get magnitude spectrum
        spectrogram[i] = getMagnitudeSpectrum(windowSpectrum.slice(0, windowSize));
    }
    return spectrogram;
}

function extractPeaks(spectrogram, filterSize, coeff) {
    const peaks = [];
    const filterEdgeSize = Math.floor((filterSize - 1) / 2);

    for (let x = 0; x < spectrogram.length; x++) {
        for (let y = 0; y < spectrogram[0].length; y++) {
            let localMaximum = 0.0;

            // Checking the neighboring values within the filter size
            for (let innerX = Math.max(0, x - filterEdgeSize); innerX <= Math.min(x + filterEdgeSize, spectrogram.length - 1); innerX++) {
                for (let innerY = Math.max(0, y - filterEdgeSize); innerY <= Math.min(y + filterEdgeSize, spectrogram[0].length - 1); innerY++) {
                    if (spectrogram[innerX][innerY] > localMaximum) {
                        localMaximum = spectrogram[innerX][innerY];
                    }
                }
            }

            // If current value is a local maximum, add it to peaks
            if (spectrogram[x][y] === localMaximum) {
                peaks.push(new Peak(y, x));  // Store (frequency, time) as a peak
            }
        }
    }

    // Calculate the mean of peak values
    const mean = peaks.reduce((acc, p) => acc + spectrogram[p.time][p.frequency], 0) / peaks.length;

    // Filter peaks based on the coefficient
    const filteredPeaks = peaks.filter(p => spectrogram[p.time][p.frequency] > mean * coeff);

    return filteredPeaks;
}

module.exports={
    getSpectrogram,
    extractPeaks
}


// Example usage
// const inputSignal = new Array(1024).fill(0).map(() => Math.random());  // Example random signal
// const windowSize = 256;
// const hopSize = 128;
// const spectrogram = getSpectrogram(inputSignal, windowSize, hopSize);
// console.log(spectrogram);
