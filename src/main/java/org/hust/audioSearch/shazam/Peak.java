package shazam;

public class Peak {
    public short frequency;
    public int time;

    public Peak(short frequency, int time){
        this.frequency = frequency;
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("Frequency: %s Time: %s", this.frequency, this.time);
    }
}