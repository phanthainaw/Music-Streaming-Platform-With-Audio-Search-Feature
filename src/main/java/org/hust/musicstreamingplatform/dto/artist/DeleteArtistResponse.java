package org.hust.musicstreamingplatform.dto.artist;

import lombok.Builder;
import lombok.Data;
import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.model.Artist;

import java.util.List;

@Data
@Builder
public class DeleteArtistResponse {
    List<TrackDto> toDeleteTracks;
    List<AlbumDto> toDeleteAlbums;
}
