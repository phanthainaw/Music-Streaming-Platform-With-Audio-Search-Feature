const fs = require('fs');
const wav = require('wav-decoder');
const { execSync } = require('child_process');
const path = require('path');
const os = require('os');

/**
 * Convert unsupported WAV format to 16-bit PCM mono 44.1kHz using ffmpeg
 * @param {string} inputPath 
 * @returns {string} path to converted WAV file
 */
function convertWavWithFfmpeg(inputPath) {
    const tempOut = path.join(os.tmpdir(), `converted-${Date.now()}.wav`);
    const cmd = `ffmpeg -y -i "${inputPath}" -acodec pcm_s16le -ac 1 -ar 44100 "${tempOut}"`;
    execSync(cmd);
    return tempOut;
}

/**
 * Extract PCM samples from WAV file
 * @param {string} filePath - path to the audio file (WAV)
 * @param {boolean} normalized - whether to normalize values to [-1, 1]
 * @returns {Promise<number[]>}
 */
async function extractPCM(filePath, normalized = false) {
    let buffer = fs.readFileSync(filePath);

    let audioData;
    try {
        audioData = await wav.decode(buffer);
    } catch (err) {
        // If decode fails, try converting using ffmpeg
        console.warn('Unsupported format. Attempting to convert with ffmpeg...');
        const convertedPath = convertWavWithFfmpeg(filePath);
        buffer = fs.readFileSync(convertedPath);
        audioData = await wav.decode(buffer);
        // Clean up temp file
        fs.unlinkSync(convertedPath);
    }

    const { channelData, bitDepth } = audioData;
    const samples = channelData[0]; // Use channel 0

    if (!normalized) {
        const maxVal = Math.pow(2, bitDepth - 1);
        return samples.map(sample => Math.round(sample * maxVal));
    }

    return Array.from(samples);
}

function toWav(pcmSignals, outputFilePath, sampleRate) {
    const byteBuffer = Buffer.alloc(pcmSignals.length * 2); // 2 bytes per sample (16-bit audio)

    // Write the WAV header
    const buffer = Buffer.alloc(44); // WAV header size
    buffer.write('RIFF', 0); // Chunk ID
    buffer.writeUInt32LE(36 + byteBuffer.length, 4); // Chunk size (36 + data size)
    buffer.write('WAVE', 8); // Format
    buffer.write('fmt ', 12); // Subchunk1 ID
    buffer.writeUInt32LE(16, 16); // Subchunk1 size (PCM format)
    buffer.writeUInt16LE(1, 20); // Audio format (1 = PCM)
    buffer.writeUInt16LE(1, 22); // Number of channels (Mono)
    buffer.writeUInt32LE(sampleRate, 24); // Sample rate
    buffer.writeUInt32LE(sampleRate * 2, 28); // Byte rate (sampleRate * numChannels * bytesPerSample)
    buffer.writeUInt16LE(2, 32); // Block align (numChannels * bytesPerSample)
    buffer.writeUInt16LE(16, 34); // Bits per sample (16-bit)
    buffer.write('data', 36); // Subchunk2 ID
    buffer.writeUInt32LE(byteBuffer.length, 40); // Subchunk2 size (data size)

    // Write PCM data
    for (let i = 0; i < pcmSignals.length; i++) {
        const scaledSample = Math.max(-1, Math.min(1, pcmSignals[i])) * 32767; // Normalize signal to short range
        const sample = Math.round(scaledSample);
        byteBuffer.writeInt16LE(sample, i * 2);
    }

    // Combine WAV header and audio data
    const wavBuffer = Buffer.concat([buffer, byteBuffer]);

    // Write the WAV file
    fs.writeFileSync(outputFilePath, wavBuffer);
}

function downsample(inputSignal, scale) {
    const newLength = Math.floor(inputSignal.length / scale); 
    const downsampledSignal = new Array(newLength);  
    for (let i = 0; i < newLength; i++) {
        downsampledSignal[i] = inputSignal[i * scale]; 
    }

    return downsampledSignal;
}



module.exports = {
    extractPCM,
    toWav,
    downsample
};
