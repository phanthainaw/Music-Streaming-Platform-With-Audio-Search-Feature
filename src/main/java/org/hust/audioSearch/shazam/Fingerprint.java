package shazam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fingerprint {
    public static Map<Integer, ArrayList<Couple>> generateFingerprints(Peak[]peaks, int songId){
        Map<Integer,ArrayList<Couple>> fingerprints = new HashMap<>();
        int targetZoneSize = 5;
        for (int anchorIndex = 0; anchorIndex < peaks.length-3-targetZoneSize; anchorIndex++) {
            for (int pointIndex = anchorIndex+2; pointIndex < anchorIndex+3  +targetZoneSize; pointIndex++) {
                int address = 0;
                Peak anchor = peaks[anchorIndex];
                Peak point = peaks[pointIndex];

                address = address|(anchor.frequency&0x1ff);
                address = address<<9|(point.frequency&0x1ff);
                address = address<<14|((point.time - anchor.time)&0x3fff);
                fingerprints.computeIfAbsent(address, k -> new ArrayList<>())
                        .add(new Couple(anchor.time, songId));
            }
        }
        return fingerprints;
    }
}
