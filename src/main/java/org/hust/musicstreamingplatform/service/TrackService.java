package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.model.Artist;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.repository.AlbumRepository;
import org.hust.musicstreamingplatform.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    public List<TrackDto> searchByTitle(String title) {
        List<Track> matchingTracks = trackRepository.searchByTitle(title);
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
                    .genre(track.getGenre().getTitle())
                    .coverUrl(track.getCoverUrl())
                    .build();
        }).toList();
    }



}

