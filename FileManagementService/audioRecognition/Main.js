const { extractPCM, downsample } = require("./service/Audio");
const filter = require("./service/Filter");
const { plotFFTResult, plotSpectrogram, plotPeaks } = require("./util/PlotUtils");
const { getSpectrogram, extractPeaks } = require("./service/Spectrogram");
const { generateFingerprints } = require("./service/Fingerprint");
const { Mongo } = require("./db/Mongo");

(async () => {
  try {
    const sampleRate = 44100;
    const outputFilePath = 'output.wav';
    const pcm = await extractPCM("./uploads/Track/audio1.wav", true);
    const filteredAudio = filter.SincFilter(pcm, 5000, sampleRate)
    const downsampledAudio = downsample(filteredAudio, 4);
    const spectrogram = getSpectrogram(downsampledAudio, 1024, 1024 / 2);
    const peaks = extractPeaks(spectrogram, 7, 1.7);
    // plotSpectrogram(spectrogram);
    // plotPeaks(peaks,spectrogram );
    const fingerprints = generateFingerprints(peaks, 0);
    const mongo = new Mongo('mongodb://localhost:27017/MusicStreamingPlatform');
    await mongo.connect();
    await mongo.insertFingerprints(fingerprints);
    // documents = await mongo.getAllDocuments('AddressHashTable');
    // console.log();
    await mongo.close()
  } catch (err) {
    console.error(err);
  }
})();
