function FFT(input) {
    // Convert real input to complex array
    let complexArray = input.map(v => ({ re: v, im: 0 }));

    // Perform recursive FFT
    let fftResult = recursiveFFT(complexArray);
    return fftResult;
}

function recursiveFFT(complexArray) {
    const N = complexArray.length;
    if (N <= 1) {
        return complexArray;
    }

    // Split into even and odd parts
    let even = [];
    let odd = [];
    for (let i = 0; i < N / 2; i++) {
        even.push(complexArray[2 * i]);
        odd.push(complexArray[2 * i + 1]);
    }

    // Recursively apply FFT to both even and odd
    even = recursiveFFT(even);
    odd = recursiveFFT(odd);

    let fftResult = new Array(N);
    for (let k = 0; k < N / 2; k++) {
        const t = {
            re: Math.cos(-2 * Math.PI * k / N),
            im: Math.sin(-2 * Math.PI * k / N),
        };

        fftResult[k] = {
            re: even[k].re + t.re * odd[k].re - t.im * odd[k].im,
            im: even[k].im + t.re * odd[k].im + t.im * odd[k].re,
        };

        fftResult[k + N / 2] = {
            re: even[k].re - t.re * odd[k].re + t.im * odd[k].im,
            im: even[k].im - t.re * odd[k].im - t.im * odd[k].re,
        };
    }

    return fftResult;
}
