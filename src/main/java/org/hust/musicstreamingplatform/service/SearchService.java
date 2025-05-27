package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.dto.artist.ArtistDto;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchService {

    private final TrackService trackService;
    private final AlbumService albumService;
    private final ArtistService artistService;

    public List<AlbumDto> searchAlbums(String query) {
        return albumService.searchByTitle(query);
    }

    public List<TrackDto> searchTracks(String query) {
        return trackService.searchByTitle(query);
    }

    public List<ArtistDto> searchArtists(String query) {
        return artistService.searchByName(query);
    }

//    public List<ArtistD> searchArtists() {
//        return new SearchArtistsResponse();
//    }
}




