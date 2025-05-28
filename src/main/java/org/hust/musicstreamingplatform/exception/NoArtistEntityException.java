package org.hust.musicstreamingplatform.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.dto.artist.ArtistDto;
import org.hust.musicstreamingplatform.dto.track.TrackDto;

import java.util.List;

@Data
@NoArgsConstructor
public class NoArtistEntityException extends RuntimeException {
    List<TrackDto> noArtistTracks;
    List<AlbumDto> noArtistAlbums ;
}
