package org.hust.audioSearch.shazam;

public class Peak {
    public short frequency;
    public double time;

    public Peak(short frequency, double time){
        this.frequency = frequency;
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("Frequency: %s Time: %s", this.frequency, this.time);
    }
}