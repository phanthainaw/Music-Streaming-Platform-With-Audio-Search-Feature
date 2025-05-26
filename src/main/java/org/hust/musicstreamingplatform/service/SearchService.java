package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.search.SearchAlbumsResponse;
import org.hust.musicstreamingplatform.dto.search.SearchArtistsResponse;
import org.hust.musicstreamingplatform.dto.search.SearchTracksResponse;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.model.Artist;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchService {

    private final TrackService trackService;
    private final AlbumService albumService;
    private final ArtirtService artService;

    public SearchAlbumsResponse searchAlbums() {

        return new SearchAlbumsResponse();
    }

    public SearchTracksResponse searchTracks(String query) {
        List<TrackDto> matchingTracks = trackService.searchTracksByTitle(query);
        return SearchTracksResponse.builder().tracks(matchingTracks).build();
    }

    public SearchArtistsResponse searchArtists() {

        return new SearchArtistsResponse();
    }
}




