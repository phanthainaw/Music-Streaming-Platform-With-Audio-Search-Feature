package org.hust.audioSearch.shazam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fingerprint {
    public static Map<Integer, ArrayList<Couple>> generateFingerprints(Peak[]peaks, int songId){
        Map<Integer,ArrayList<Couple>> fingerprints = new HashMap<>();
        int targetZoneSize = 5;
        for (int anchorIndex = 0; anchorIndex < peaks.length-2-targetZoneSize; anchorIndex++) {
            for (int pointIndex = anchorIndex+3; pointIndex < anchorIndex+ 3+targetZoneSize; pointIndex++) {
                int address = 0;
                Peak anchor = peaks[anchorIndex];
                Peak point = peaks[pointIndex];
                address = address|(anchor.frequency&0x1ff);
                address = address<<9|(point.frequency&0x1ff);
                address = address<<14|((int)(point.time - anchor.time)&0x3fff);
                fingerprints.computeIfAbsent(address, key -> new ArrayList<>())
                        .add(new Couple( (int)(anchor.time), songId));
//                System.out.println(String.format("%s, %s, %s", anchor.frequency, point.frequency, point.time - anchor.time));
            }
        }
        return fingerprints;
    }
}
