package org.hust.musicstreamingplatform.exception.track;

import org.hust.musicstreamingplatform.dto.track.TrackDto;

import java.util.List;

public class NoArtistTrackException extends RuntimeException {
    public List<TrackDto> NoArtistTracks;

    public NoArtistTrackException(List<TrackDto> NoArtistTracks) {
        this.NoArtistTracks = NoArtistTracks;
    }
}
