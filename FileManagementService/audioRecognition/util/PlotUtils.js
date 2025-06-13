const { createCanvas } = require('canvas');
const fs = require('fs');

function plotFFTResult(fftResult) {
    const width = 800;
    const height = 400;

    // Create a canvas
    const canvas = createCanvas(width, height);
    const ctx = canvas.getContext('2d');

    // Clear the canvas
    ctx.clearRect(0, 0, width, height);

    // Set up the graph
    ctx.beginPath();
    ctx.moveTo(0, height / 2); // Start at the middle of the canvas

    // Loop through the FFT result and plot each point
    for (let i = 1; i < fftResult.length / 2; i++) { // Only plot the positive frequencies
        // Calculate magnitude (sqrt(real^2 + imaginary^2))
        const real = fftResult[2 * i];
        const imaginary = fftResult[2 * i + 1];
        const magnitude = Math.sqrt(real * real + imaginary * imaginary);

        const x = (i / (fftResult.length / 2)) * width; // Calculate x position (normalized for width)
        const y = (magnitude * height) / 10; // Scale and shift y position

        ctx.lineTo(x, height - y); // Invert the y-axis so it grows upwards
    }

    // Style the line
    ctx.strokeStyle = 'rgb(75, 192, 192)'; // Line color
    ctx.lineWidth = 2;
    ctx.stroke(); // Actually draw the line

    // Save the canvas to a file
    const out = fs.createWriteStream('fft_plot.png');
    const stream = canvas.createPNGStream();
    stream.pipe(out);
    out.on('finish', () => console.log('The FFT plot was saved as fft_plot.png'));
}

// Function to plot spectrogram
function plotSpectrogram(spectrogramData) {
    const width = 800;
    const height = 400;

    // Create a canvas
    const canvas = createCanvas(width, height);
    const ctx = canvas.getContext('2d');

    // Clear the canvas
    ctx.clearRect(0, 0, width, height);

    // Set the number of time steps (columns) and frequency bins (rows)
    const numTimeSteps = spectrogramData.length;
    const numFreqBins = spectrogramData[0].length;

    // Scale factors
    const xScale = width / numTimeSteps; // Scale x-axis for time
    const yScale = height / numFreqBins; // Scale y-axis for frequency bins

    // Loop through the spectrogram data and plot each point
    for (let t = 0; t < numTimeSteps; t++) { // Loop through time steps (columns)
        for (let f = 0; f < numFreqBins; f++) { // Loop through frequency bins (rows)
            const magnitude = spectrogramData[t][f]; // Get the magnitude

            // Map the magnitude to a color (using a color scale)
            const color = getColorForMagnitude(magnitude);

            // Plot each cell as a small rectangle
            ctx.fillStyle = color;
            ctx.fillRect(t * xScale, height - (f * yScale), xScale, yScale);
        }
    }

    // Save the canvas to a file
    const out = fs.createWriteStream('spectrogram.png');
    const stream = canvas.createPNGStream();
    stream.pipe(out);
    out.on('finish', () => console.log('The spectrogram plot was saved as spectrogram.png'));
}

// Function to map magnitude to color (using a simple color scale)
function getColorForMagnitude(magnitude) {
    // Ensure magnitude is within range [0, 1]
    const normalized = Math.min(Math.max(magnitude, 0), 1);

    // Apply a logarithmic scale for better visualization of small values
    const logMagnitude = Math.log10(normalized * (1 - Number.MIN_VALUE) + 1); // Ensure no log(0) by adding a small value

    // Normalize logMagnitude to the range [0, 1]
    const normalizedLog = logMagnitude / Math.log10(2); // Logarithmically normalize it to 0-1 range

    // Generate RGB values based on logarithmic magnitude
    const r = Math.floor(normalizedLog * 255);  // Red increases with magnitude
    const g = Math.floor((1 - normalizedLog) * 255);  // Green decreases with magnitude
    const b = Math.floor(255 - normalizedLog * 255); // Blue decreases as magnitude increases

    return `rgb(${r}, ${g}, ${b})`;
}

function plotPeaks(peaks, spectrogramData) {
    const width = 800;
    const height = 400;

    // Create a canvas
    const canvas = createCanvas(width, height);
    const ctx = canvas.getContext('2d');

    // Clear the canvas
    ctx.clearRect(0, 0, width, height);

    // Set the number of time steps (columns) and frequency bins (rows)
    const numTimeSteps = spectrogramData.length;
    const numFreqBins = spectrogramData[0].length;

    // Scale factors
    const xScale = width / numTimeSteps; // Scale x-axis for time
    const yScale = height / numFreqBins; // Scale y-axis for frequency bins

    // Loop through peaks and plot them as dots
    peaks.forEach(peak => {
        const magnitude = spectrogramData[peak.time][peak.frequency]; // Get the magnitude of the peak

        // Map the magnitude to a color (using a color scale, here using green for example)
        const color = getColorForMagnitude(magnitude);

        // Calculate the position of the peak on the canvas
        const x = peak.time * xScale; // Time step position
        const y = height - (peak.frequency * yScale); // Frequency bin position (inverted Y axis)

        // Plot each peak as a small circle
        ctx.fillStyle = color;
        ctx.beginPath();
        ctx.arc(x, y, 5, 0, Math.PI * 2); // Circle with radius 5
        ctx.fill();
    });

    // Save the canvas to a file
    const out = fs.createWriteStream('peaks_plot.png');
    const stream = canvas.createPNGStream();
    stream.pipe(out);
    out.on('finish', () => console.log('The peaks plot was saved as peaks_plot.png'));
}


module.exports = {
    plotSpectrogram
};


module.exports = {
    plotFFTResult,
    plotSpectrogram,
    plotPeaks
};
