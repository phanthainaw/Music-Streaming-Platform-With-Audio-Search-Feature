package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.search.SearchTracksResponse;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.model.Artist;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.repository.AlbumRepository;
import org.hust.musicstreamingplatform.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;

    public List<TrackDto> searchTracksByTitle(String query) {
        List<Track> matchingTracks = trackRepository.searchTracksByTitle(query);
        return matchingTracks.stream().map(track -> {
            String albumName = track.getAlbum().getTitle();
            int albumId = track.getAlbum().getId();
            List<String> artistName = track.getArtists().stream().map(Artist::getName).toList();
            List<Integer> artistId = track.getArtists().stream().map(Artist::getId).toList();
            return TrackDto.builder().id(track.getId())
                    .title(track.getTitle())
                    .duration(track.getDuration())
                    .albumId(albumId)
                    .albumName(albumName)
                    .artistsId(artistId)
                    .artistsName(artistName)
                    .build();
        }).toList();
    }
}

