package org.hust.musicstreamingplatform.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.exception.album.NoArtistAlbumException;
import org.hust.musicstreamingplatform.exception.track.NoArtistTrackException;
import org.hust.musicstreamingplatform.model.Album;
import org.hust.musicstreamingplatform.model.Artist;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.repository.AlbumRepository;
import org.hust.musicstreamingplatform.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    @Autowired private AlbumRepository albumRepository;

    @PersistenceContext
    private EntityManager entityManager;

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


    public void removeArtistFromAlbum(int artistId) {
        List<Album> albums = albumRepository.findByArtistsId(artistId);

        // Identify tracks where the artist is the only one
        List<Album> soloAlbums = albums.stream()
                .filter(album -> album.getArtists().size() == 1)
                .toList();

        if (!soloAlbums.isEmpty()) {
            // Throw an exception with the IDs of the problematic tracks
            throw new NoArtistAlbumException(soloAlbums.stream()
                    .map(AlbumService::getAlbumDto).toList());
        }
        // Proceed to remove artist from all other tracks
        albumRepository.removeArtistFromAlbums(artistId);
        albums.forEach(album -> entityManager.refresh(album));
    }

    public static AlbumDto getAlbumDto(Album album){
        return AlbumDto.builder()
                .id(album.getId())
                .title(album.getTitle())
                .coverUrl(album.getCoverUrl())
                .releaseDate(album.getReleaseDate())
                .genre(album.getGenre().getTitle())
                .build();
    }
}
