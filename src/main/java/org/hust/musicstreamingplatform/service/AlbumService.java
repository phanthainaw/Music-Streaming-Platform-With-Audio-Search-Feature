package org.hust.musicstreamingplatform.service;

import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.model.Album;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    @Autowired private AlbumRepository albumRepository;

    public List<AlbumDto> searchByTitle(String q) {
        List<Album> matchingAlbums = albumRepository.searchByTitle(q);

        return matchingAlbums.stream().map(album ->  AlbumDto.builder()
                .id(album.getId())
                .title(album.getTitle())
                .releaseDate(album.getReleaseDate())
                .genre(album.getGenre().getTitle())
                .coverUrl(album.getCoverUrl())
                .build()).toList();
    }



}
