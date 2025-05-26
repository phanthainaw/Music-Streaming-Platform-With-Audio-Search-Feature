package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.search.SearchTracksResponse;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    private List<SearchTracksResponse> searchTrack(){
        List<Track> matchingTracks = trackRepository.searchTracksByTitle("");


        return ;
    } ;


}
