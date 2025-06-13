const { Couple } = require('../model/Couple')

function generateFingerprints(peaks, songId) {
    const fingerprints = new Map();
    const targetZoneSize = 5;

    for (let anchorIndex = 0; anchorIndex < peaks.length - 2 - targetZoneSize; anchorIndex++) {
        for (let pointIndex = anchorIndex + 2; pointIndex < anchorIndex + 2 + targetZoneSize; pointIndex++) {
            let address = 0;
            const anchor = peaks[anchorIndex];
            const point = peaks[pointIndex];

            address |= anchor.frequency & 0x1ff; // Mask the frequency to the lower 9 bits
            address <<= 9;
            address |= point.frequency & 0x1ff; // Mask the frequency to the lower 9 bits
            address <<= 14;
            address |= (point.time - anchor.time) & 0x3fff; // Mask the time difference to the lower 14 bits

            // Check if the address already exists in the map, if not, initialize it
            if (!fingerprints.has(address)) {
                fingerprints.set(address, []);
            }

            // Add the Couple object to the list corresponding to the address
            fingerprints.get(address).push(new Couple(anchor.time, songId));
        }
    }

    return fingerprints;
}

module.exports = {
    generateFingerprints
}