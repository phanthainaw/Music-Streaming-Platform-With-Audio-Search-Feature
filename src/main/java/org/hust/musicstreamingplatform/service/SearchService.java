package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.search.SearchAlbumsResponse;
import org.hust.musicstreamingplatform.dto.search.SearchArtistsResponse;
import org.hust.musicstreamingplatform.dto.search.SearchTracksResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchService {

    private final TrackService trackService;
    private final AlbumService albumService;
    private final ArtirtService artService;

    public SearchAlbumsResponse searchAlbums(){

        return new SearchAlbumsResponse();
    }

    public SearchTracksResponse searchTracks(){
        return new SearchTracksResponse();
    }

    public SearchArtistsResponse searchArtist(){

        return new SearchArtistsResponse();
    }

}
