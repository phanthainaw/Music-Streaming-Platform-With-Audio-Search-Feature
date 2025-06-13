package org.hust.audioSearch.shazam;

import javax.sound.sampled.*;
import java.io.*;

public class Audio {
    public static double[] extractPCM(File audioFile, boolean normalized) throws UnsupportedAudioFileException {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            AudioFormat format = audioStream.getFormat();
            int sampleSize = format.getSampleSizeInBits() / 8;
            if (sampleSize > 4) throw new UnsupportedAudioFileException("Do not support audio with bit depth more than 4 bytes");
            boolean isSigned = format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;
            boolean isBigEndian = format.isBigEndian();
            byte[] audioBytes;
            try {
                audioBytes = audioStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int totalSamples = audioBytes.length / sampleSize;
            double[] pcmValues = new double[totalSamples];
            double maxVal = isSigned
                    ? Math.pow(2, format.getSampleSizeInBits() - 1)
                    : Math.pow(2, format.getSampleSizeInBits()) - 1;
            //Assemble byte
            for (int sampleNo = 0; sampleNo < totalSamples; sampleNo++) {
                int pcmValue = 0;
                for (int byteNo = 0; byteNo < sampleSize; byteNo++) {
                    int currentByte = (audioBytes[sampleNo * sampleSize + byteNo] & 0xFF);
                    pcmValue |= currentByte << (isBigEndian ? (sampleSize - 1 - byteNo) * 8 : (byteNo * 8));
                }
                if (isSigned) {
                    switch (sampleSize) {
                        case 1:
                            pcmValues[sampleNo] = (byte) pcmValue;
                            break;
                        case 2:
                            pcmValues[sampleNo] = (short) pcmValue;
                            break;
                        case 3:
                            pcmValues[sampleNo] = (pcmValue << 8) >> 8;
                            break;
                        default:
                            pcmValues[sampleNo] = pcmValue;
                    }
                } else {
                    switch (sampleSize) {
                        case 4:
                            pcmValues[sampleNo] = Integer.parseInt(Integer.toUnsignedString(pcmValue));
                            break;
                        default:
                            pcmValues[sampleNo] = pcmValue;
                    }
                }
                if (normalized) pcmValues[sampleNo] = pcmValues[sampleNo] / maxVal;
            }
            return pcmValues;


        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AudioFormat getAudioFormat(File audioFile) throws UnsupportedAudioFileException, IOException {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            return audioStream.getFormat();
        }
    }

    public static double[] downsample(double[] inputSignal, int scale){
        int size = inputSignal.length/scale;
        double[] downsampledSignal = new double[size];

        for (int i = 0; i < size; i++){
            downsampledSignal[i]= inputSignal[i*scale];
        }
        return downsampledSignal;
    }

    public static void toWav(double[] pcmSignals, File outputFile, int sampleRate ) throws IOException {
        byte[] byteBuffer = new byte[pcmSignals.length*2];

        AudioFormat targetFormat = new AudioFormat(sampleRate,
                16,
                1,
                true,
                false);
        for (int i = 0; i < pcmSignals.length; i++) {
            short scaledSample = (short)(pcmSignals[i]* Short.MAX_VALUE);
            byteBuffer[2*i] = (byte) scaledSample;
            byteBuffer[2*i+1] = (byte) ( scaledSample>>8);
        }
        ByteArrayInputStream bytes = new ByteArrayInputStream(byteBuffer);
        AudioInputStream audioInputStream = new AudioInputStream(bytes, targetFormat,pcmSignals.length);
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);
    }

    public static void convert(String inputMp3, String outputWav) {
        String[] command = {
                "C:\\Users\\Admin\\AppData\\Local\\Microsoft\\WinGet\\Links\\ffmpeg.exe",
                "-i", inputMp3,
                "-acodec", "pcm_s32le", // 32-bit little endian
                "-ac", "1",             // mono
                "-ar", "44100",         // 44100 Hz
                outputWav
        };

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Output ffmpeg messages (optional)
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Conversion completed: " + outputWav);
            } else {
                System.err.println("ffmpeg failed with exit code " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Conversion error.");
        }
    }

    public static double getAudioDuration(File audioFile){
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            AudioFormat format = audioStream.getFormat();
            long frames = audioStream.getFrameLength();
            double durationInSeconds = (frames + 0.0) / format.getFrameRate();
            return durationInSeconds;
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
