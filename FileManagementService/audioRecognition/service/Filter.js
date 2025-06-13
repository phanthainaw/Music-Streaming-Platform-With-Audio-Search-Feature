const FFT = require("fft.js");
function createSincKernel(length, cutOffFreq) {
  let sincKernel = new Array(length);
  for (i = 0; i < length; i++) {
    let offset = (length - 1) / 2;
    let time = i - offset;
    let sinc =
      time == 0
        ? 2 * cutOffFreq
        : Math.sin(2 * Math.PI * cutOffFreq * time) / (time * Math.PI);
    let window = 0.54 - 0.46 * Math.cos((2 * Math.PI * i) / (length - 1));
    sincKernel[i] = sinc * window;
  }
  return sincKernel;
}

function multiplyComplexSpectra(a, b) {
  size = a.length;
  result = new Array(size);
  result[0] = a[0] * b[0];
  result[1] = a[1] * b[1];

  for (i = 1; i < size / 2; i++) {
    real = a[2 * i] * b[2 * i] - a[2 * i + 1] * b[2 * i + 1];
    imaginary = a[2 * i] * b[2 * i + 1] + a[2 * i + 1] * b[2 * i];
    result[2 * i] = real;
    result[2 * i + 1] = imaginary;
  }

  return result;
}

function getMagnitudeSpectrum(spectra) {
  size = spectra.length / 2;
  magnitudeSpectrum = new Array(size);
  for (i = 0; i < size; i++) {
    re = spectra[2 * i];
    im = spectra[2 * i + 1];
    magnitudeSpectrum[i] = Math.sqrt(re * re + im * im);
  }
  return magnitudeSpectrum;
}

function sincFilter(inputSignal, cutOffFrequency, sampleRate) {

  const originalSignalLength = inputSignal.length;
  const nearestPowerOf2SignalLength = Math.pow(2, Math.ceil(Math.log(inputSignal.length) / Math.log(2)));

  const fft = new FFT(nearestPowerOf2SignalLength);

  let sincKernel = createSincKernel(512, cutOffFrequency / sampleRate);
  sincKernel = copyAndResize(sincKernel, nearestPowerOf2SignalLength);
  inputSignal = copyAndResize(inputSignal, nearestPowerOf2SignalLength);

  sincKernel = fft.toComplexArray(sincKernel);
  inputSignal = fft.toComplexArray(inputSignal);

  const kernelFFT = fft.createComplexArray();
  const inputFFT = fft.createComplexArray();

  fft.transform(kernelFFT, sincKernel);
  fft.transform(inputFFT, inputSignal);

  let filteredAudioFFT = multiplyComplexSpectra(kernelFFT, inputFFT);

    // return filteredAudioFFT;

  let outputSignal = fft.createComplexArray();
  fft.inverseTransform(outputSignal, filteredAudioFFT);

  outputSignal = outputSignal.filter((_, idx) => idx % 2 == 0)

  return copyAndResize(outputSignal, originalSignalLength);
}

function copyAndResize(arr, newLength, fillValue = 0) {
  const result = arr.slice(0, newLength);
  while (result.length < newLength) {
    result.push(fillValue);
  }
  return result;
}

module.exports = {
  sincFilter,
  getMagnitudeSpectrum
};
