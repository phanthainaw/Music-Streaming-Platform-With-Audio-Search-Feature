package org.hust.musicstreamingplatform.exception.album;

import lombok.Data;
import org.hust.musicstreamingplatform.dto.album.AlbumDto;

import java.util.List;

@Data
public class NoArtistAlbumException extends RuntimeException {
    public List<AlbumDto> NoArtistAlbums;

    public NoArtistAlbumException(List<AlbumDto> NoArtistAlbums) {
        this.NoArtistAlbums = NoArtistAlbums;
    }
}
