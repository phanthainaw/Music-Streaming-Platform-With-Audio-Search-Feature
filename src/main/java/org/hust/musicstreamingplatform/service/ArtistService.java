package org.hust.musicstreamingplatform.service;

import org.hust.musicstreamingplatform.dto.artist.ArtistDto;
import org.hust.musicstreamingplatform.model.Artist;
import org.hust.musicstreamingplatform.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<ArtistDto> searchByName(String name) {
        List<Artist> artists = artistRepository.searchByName(name);
        return artists.stream().map(artist -> ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .bio(artist.getBio())
                .country(artist.getCountry())
                .avatarUrl(artist.getAvatarUrl())
                .build()).toList();
    }
}
