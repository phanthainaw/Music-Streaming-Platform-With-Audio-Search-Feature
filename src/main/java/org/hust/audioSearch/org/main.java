package org.hust.audioSearch.org;

import org.hust.audioSearch.shazam.Shazam;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import static org.Utils.getBaseName;

public class main {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        File audioDir = new File("./SongStorage");
        File[] audioFileList = audioDir.listFiles();
        for (File audioFile : audioFileList ){
            int currentSongId = Integer.parseInt(getBaseName (audioFile.getName()));
            Shazam.addAudio(audioFile, currentSongId);
            System.out.println("Song "+currentSongId+" is added to the database");
        }
//        File audioDir = new File("./RecordSample");
//        File[] audioFileList = audioDir.listFiles();
//        for (File audioFile : audioFileList ){
//            int matchedSongId = Shazam.matchAudio(audioFile);
//            System.out.println(String.format("Record %s matched %s", audioFile.getName(), matchedSongId));
//        }
//        System.exit(0);
//        File audioDir = new File("./UnconvertedAudio");
//        File[] audioFileList = audioDir.listFiles();
//        int count = 1;
//        for (File audioFile : audioFileList ){
//            System.out.println(String.format("\"%s\"", audioFile.getCanonicalPath()));
//            convert(String.format("\"%s\"", audioFile.getCanonicalPath()), String.format("./SongStorage/%s.wav",count));
//            count++;
//        }
    }
}
