package org.hust.audioSearch.shazam;
import org.hust.audioSearch.db.Mongo;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hust.audioSearch.shazam.Audio.*;
import static org.hust.audioSearch.shazam.FFT.*;
import static org.hust.audioSearch.shazam.Fingerprint.generateFingerprints;
import static org.hust.audioSearch.shazam.Spectrogram.*;

public class Shazam {
    public static int matchAudio (File audioFile) throws UnsupportedAudioFileException, IOException {
        double[] preprocessedSignal = preprocessAudio(audioFile, 4, 5000.0);
//        preprocessedSignal=highPassFilter(preprocessedSignal,100, 11025);
        double[][] spectrogram = getSpectrogram(preprocessedSignal, 1024, 1024/2);
        Peak[] spectrogramPeaks = extractPeak(spectrogram, 7, 1.7);
        System.out.println(spectrogramPeaks.length);
        Map<Integer, ArrayList<Couple>> audioFingerprints = generateFingerprints(spectrogramPeaks, 0);
        Integer[] hashes = audioFingerprints.keySet().toArray(new Integer[0]);
        Mongo mongo = new Mongo("mongodb://127.0.0.1:27017", "MusicStreamingPlatform");
        Map<Integer, ArrayList<Couple>> matchingFingerprints = mongo.getMatchingCouples(hashes);
        mongo.close();
        return filter(matchingFingerprints, audioFingerprints);
    }

    public static void addAudio(File audioFile, int songId) throws UnsupportedAudioFileException, IOException {
        double[] preprocessedSignal = preprocessAudio(audioFile, 4, 5000.0);
        double[][] spectrogram = getSpectrogram(preprocessedSignal, 1024, 1024/2);
        Peak[] spectrogramPeaks = extractPeak(spectrogram, 7, 1.5);
        Map<Integer, ArrayList<Couple>> audioFingerprints = generateFingerprints(spectrogramPeaks, songId);
        Mongo mongo = new Mongo("mongodb://127.0.0.1:27017", "MusicStreamingPlatform");
        mongo.insertFingerprints(audioFingerprints);
    }

    public static double[] preprocessAudio(File audioFile, int downsampleRatio, double cutOffFreq ) throws UnsupportedAudioFileException, IOException {
        AudioFormat format = Audio.getAudioFormat(audioFile);
        double[] PCMSignals = Audio.extractPCM(audioFile, true);
        double[] filteredSignals = lowPassFilter(PCMSignals, cutOffFreq, format.getSampleRate());
//        double[] filteredSignals = SincFilter(PCMSignals,cutOffFreq, format.getSampleRate());
        return downsample(filteredSignals, downsampleRatio);
    }

    public static int filter(Map<Integer, ArrayList<Couple>> matchedHashCoupleMap, Map<Integer, ArrayList<Couple>> recordHashCoupleMap) {
        Map<Couple, Integer> coupleCount = new HashMap<>();

        for (Map.Entry<Integer, ArrayList<Couple>> entry : matchedHashCoupleMap.entrySet()) {
            for (Couple matchedCouple : entry.getValue()) {
                coupleCount.compute(matchedCouple, (key, val) -> val == null ? 1 : val + 1);
            }
        }

        //Delete couples which doesn't form target zone
        coupleCount.entrySet().removeIf(entry -> entry.getValue() < 5);

        //Count target zones
        Map<Integer, Integer> targetZoneCount = new HashMap<>();
        for (Map.Entry<Couple, Integer> entry : coupleCount.entrySet()) {
            targetZoneCount.compute(entry.getKey().songId, (key, val) -> val == null ? 1 : val + 1);
        }
        int NumOfSongWithMostTargetZone = 10;

        List<Integer> remainingSongIdList = new ArrayList<>();
        List<Integer> coupleCountList = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : targetZoneCount.entrySet()) {
            int songId = entry.getKey();
            int count = entry.getValue();

            // Add the new entry
            remainingSongIdList.add(songId);
            coupleCountList.add(count);

            // If we now have more than 4, remove the one with the smallest count
            if (coupleCountList.size() > NumOfSongWithMostTargetZone) {
                int minIndex = 0;
                for (int i = 1; i < coupleCountList.size(); i++) {
                    if (coupleCountList.get(i) < coupleCountList.get(minIndex)) {
                        minIndex = i;
                    }
                }
                remainingSongIdList.remove(minIndex);
                coupleCountList.remove(minIndex);
            }
        }
        //Calculate time coherency scores

        final int MAX_TIME_OFFSETS = 2000;

        Map<Integer, ArrayList<Integer>> timeCoherency = new HashMap<>();
        for (Map.Entry<Integer, ArrayList<Couple>> entry : recordHashCoupleMap.entrySet()) {
                for (Couple recordCouple : entry.getValue()) {
                    if (matchedHashCoupleMap.get(entry.getKey())==null) continue;
                    for (Couple dbCouple : matchedHashCoupleMap.get(entry.getKey())) {
                        if (!remainingSongIdList.contains(dbCouple.songId)) continue;
                        timeCoherency.compute(dbCouple.songId, (key, val) -> {
                            if (val == null) val = new ArrayList<>();
                            if (val.size() < MAX_TIME_OFFSETS){
                                val.add(recordCouple.time - dbCouple.time);}
                            return val;
                        });

                    }
                }
        }

        int highestNumOfCoherenceTimeDelta=0;
        Map<Integer, Integer> highestCoherenceTimeDelta = new HashMap<>();
        for (Map.Entry<Integer, ArrayList<Integer>> entry : timeCoherency.entrySet()){
            Map<Integer, Integer> timeDeltaCount = new HashMap<>();
            int maxCount = 0;
            for (Integer timeDelta : entry.getValue()){
                timeDeltaCount.compute(timeDelta, (key, val) -> val == null ? 1 : val + 1);
                int currentCount = timeDeltaCount.get(timeDelta);
                if (currentCount>maxCount) maxCount = currentCount;
            }
            highestCoherenceTimeDelta.put(entry.getKey(), maxCount);
            if (maxCount > highestNumOfCoherenceTimeDelta) highestNumOfCoherenceTimeDelta=maxCount;
        }

        int MatchedSongId = 0;
        for (Map.Entry<Integer, Integer> entry : highestCoherenceTimeDelta.entrySet()){
            if(highestNumOfCoherenceTimeDelta == entry.getValue()) MatchedSongId = entry.getKey();
            System.out.println(String.format("Song %s: %s",entry.getKey(),entry.getValue() ));
        }
        return MatchedSongId;
    }


}
