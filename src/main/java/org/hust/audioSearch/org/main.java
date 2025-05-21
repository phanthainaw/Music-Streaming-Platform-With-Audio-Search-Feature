package org.hust.audioSearch.org;

import org.hust.audioSearch.shazam.Shazam;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hust.audioSearch.org.Utils.getBaseName;


public class main {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        AtomicInteger correctGuess= new AtomicInteger();

        File audioDir = new File("./RecordSample");
        File[] audioFileList = audioDir.listFiles();

        if (audioFileList == null || audioFileList.length == 0) {
            System.out.println("No audio files found.");
            return;
        }

        // Create a fixed thread pool (adjust the number of threads as needed)
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (File audioFile : audioFileList) {
            executor.submit(() -> {
                try {
//                    int currentSongId = Integer.parseInt(getBaseName(audioFile.getName()));
//                    Shazam.addAudio(audioFile, currentSongId);
//                    System.out.println("Song " + currentSongId + " is added to the database");

                    int matchedSongId = Shazam.matchAudio(audioFile);
                    System.out.println(String.format("Record %s matched %s", audioFile.getName(), matchedSongId));
                    if(matchedSongId == Integer.parseInt(getBaseName(audioFile.getName()))) correctGuess.getAndIncrement();
                } catch (Exception e) {
                    System.err.println("Failed to process file: " + audioFile.getName());
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS); // wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(String.format("Success rate: %s/%s", correctGuess,audioFileList.length));

//        File RecordFile = new File("./OldSongStorage/1.wav");
//        int matchedSongId = Shazam.matchAudio(RecordFile);
//        System.out.println(String.format("Record %s matched %s", RecordFile.getName(), matchedSongId));


//        File audioDir = new File("./RecordSample");
//        File[] audioFileList = audioDir.listFiles();
//        int count = 1;
//        for (File audioFile : audioFileList) {
//            convert(String.format("\"%s\"", audioFile.getCanonicalPath()), String.format("./RecordSample/%s.wav", count));
//            count++;
        }
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



