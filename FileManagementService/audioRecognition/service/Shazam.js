const { getSpectrogram, extractPeaks } = require('./Spectrogram');
const { generateFingerprints } = require('./Fingerprint');
const { downsample, extractPCM } = require('./Audio');
const { sincFilter } = require('./Filter');
const { Mongo } = require('../db/Mongo');

   async function matchAudio(audioFile) {
    try {
      const preprocessedSignal = await preprocessAudio(audioFile, 4, 5000.0);
      const spectrogram = getSpectrogram(preprocessedSignal, 1024, 1024 / 2);
      const spectrogramPeaks = extractPeaks(spectrogram, 7, 1.7);
    //   console.log(spectrogramPeaks.length);
      const audioFingerprints = generateFingerprints(spectrogramPeaks, 0);
      const hashes = Object.keys(audioFingerprints);
      const mongo = new Mongo('mongodb://127.0.0.1:27017', 'MusicStreamingPlatform');
      const matchingFingerprints = await mongo.getMatchingCouples(hashes);
      await mongo.close();
      return filter(matchingFingerprints, audioFingerprints);
    } catch (error) {
      console.error('Error matching audio:', error);
    }
  }

  async function addAudio(audioFile, songId) {
    try {
      const preprocessedSignal = await preprocessAudio(audioFile, 4, 5000.0);
      const spectrogram = getSpectrogram(preprocessedSignal, 1024, 1024 / 2);
      const spectrogramPeaks = extractPeaks(spectrogram, 7, 1.5);
      const audioFingerprints = generateFingerprints(spectrogramPeaks, songId);

      const mongo = new Mongo('mongodb://127.0.0.1:27017', 'MusicStreamingPlatform');
      await mongo.insertFingerprints(audioFingerprints);
    } catch (error) {
      console.error('Error adding audio:', error);
    }
  }

  async function preprocessAudio(audioFile, downsampleRatio, cutOffFreq) {
    try {
      const format = getAudioFormat(audioFile);
      const pcmSignals = await extractPCM(audioFile, true);
      const filteredSignals = sincFilter(pcmSignals, cutOffFreq, format.sampleRate);
      return downsample(filteredSignals, downsampleRatio);
    } catch (error) {
      console.error('Error preprocessing audio:', error);
    }
  }

   function getAudioFormat(audioFile) {
    // Replace this with actual logic to extract the audio format.
    // Use libraries like `wav-decoder` or `node-webkit` to decode audio format.
    return { sampleRate: 44100 }; // Example value
  }
  
  function filter(matchedHashCoupleMap, recordHashCoupleMap) {
    const coupleCount = {};

    // Count occurrences of each couple
    Object.entries(matchedHashCoupleMap).forEach(([key, couples]) => {
      couples.forEach((matchedCouple) => {
        coupleCount[matchedCouple] = (coupleCount[matchedCouple] || 0) + 1;
      });
    });

    // Filter couples which don't meet the threshold
    Object.keys(coupleCount).forEach((couple) => {
      if (coupleCount[couple] < 5) {
        delete coupleCount[couple];
      }
    });

    // Count target zones
    const targetZoneCount = {};
    Object.entries(coupleCount).forEach(([couple, count]) => {
      targetZoneCount[couple.songId] = (targetZoneCount[couple.songId] || 0) + 1;
    });

    const numOfSongsWithMostTargetZone = 10;
    let remainingSongIdList = [];
    let coupleCountList = [];

    Object.entries(targetZoneCount).forEach(([songId, count]) => {
      remainingSongIdList.push(songId);
      coupleCountList.push(count);

      // If we have more than the max number, remove the one with the smallest count
      if (coupleCountList.length > numOfSongsWithMostTargetZone) {
        const minIndex = coupleCountList.indexOf(Math.min(...coupleCountList));
        remainingSongIdList.splice(minIndex, 1);
        coupleCountList.splice(minIndex, 1);
      }
    });

    // Calculate time coherency scores
    const timeCoherency = {};
    const maxTimeOffsets = 2000;

    Object.entries(recordHashCoupleMap).forEach(([key, recordCouples]) => {
      if (!matchedHashCoupleMap[key]) return;

      recordCouples.forEach((recordCouple) => {
        matchedHashCoupleMap[key].forEach((dbCouple) => {
          if (remainingSongIdList.includes(dbCouple.songId)) {
            if (!timeCoherency[dbCouple.songId]) {
              timeCoherency[dbCouple.songId] = [];
            }
            if (timeCoherency[dbCouple.songId].length < maxTimeOffsets) {
              timeCoherency[dbCouple.songId].push(recordCouple.time - dbCouple.time);
            }
          }
        });
      });
    });

    let highestNumOfCoherenceTimeDelta = 0;
    const highestCoherenceTimeDelta = {};

    Object.entries(timeCoherency).forEach(([songId, timeDeltas]) => {
      const timeDeltaCount = {};
      let maxCount = 0;

      timeDeltas.forEach((timeDelta) => {
        timeDeltaCount[timeDelta] = (timeDeltaCount[timeDelta] || 0) + 1;
        maxCount = Math.max(maxCount, timeDeltaCount[timeDelta]);
      });

      highestCoherenceTimeDelta[songId] = maxCount;
      highestNumOfCoherenceTimeDelta = Math.max(highestNumOfCoherenceTimeDelta, maxCount);
    });

    let matchedSongId = 0;
    Object.entries(highestCoherenceTimeDelta).forEach(([songId, count]) => {
      if (count === highestNumOfCoherenceTimeDelta) {
        matchedSongId = songId;
      }
      console.log(`Song ${songId}: ${count}`);
    });

    return matchedSongId;
  }


module.exports = Shazam;
