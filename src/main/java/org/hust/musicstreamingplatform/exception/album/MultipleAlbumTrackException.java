package org.hust.musicstreamingplatform.exception.album;

import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.model.Track;

import java.util.List;

public class MultipleAlbumTrackException extends RuntimeException {
    public List<TrackDto> TrackWithAlbum;

    public MultipleAlbumTrackException(List<TrackDto> tracks) {
        this.TrackWithAlbum = tracks;
    }
}
