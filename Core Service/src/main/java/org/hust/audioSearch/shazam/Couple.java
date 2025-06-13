package org.hust.audioSearch.shazam;

import java.util.Objects;

public class Couple {
    public int time;
    public int songId;

    public Couple(int time, int songId){
        this.time = time;
        this.songId = songId;
    }

    @Override
    public String toString() {
        return String.format("time: %s song id: %s", this.time, this.songId);
    }
    @Override
    public boolean equals(Object obj) {
        Couple couple = (Couple) obj;
        return (this.time == couple.time)&&(this.songId == couple.songId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(time, songId);
    }
}
