package org.hust.musicstreamingplatform.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hust.musicstreamingplatform.dto.album.AddTrackToAlbumRequest;
import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.dto.album.CreateAlbumRequest;
import org.hust.musicstreamingplatform.exception.UnauthorizedUpdaterException;
import org.hust.musicstreamingplatform.exception.UserNotFoundException;
import org.hust.musicstreamingplatform.exception.album.AlbumNotFoundException;
import org.hust.musicstreamingplatform.exception.album.MultipleAlbumTrackException;
import org.hust.musicstreamingplatform.exception.album.NoArtistAlbumException;
import org.hust.musicstreamingplatform.exception.artist.ArtistNotFoundException;
import org.hust.musicstreamingplatform.exception.genre.GenreNotFoundException;
import org.hust.musicstreamingplatform.exception.track.TrackNotFoundException;
import org.hust.musicstreamingplatform.model.*;
import org.hust.musicstreamingplatform.model.enums.Role;
import org.hust.musicstreamingplatform.repository.*;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AlbumService(AlbumRepository albumRepository, GenreRepository genreRepository, TrackRepository trackRepository, ArtistRepository artistRepository, UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
    }

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

    public List<AlbumDto> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        return albums.stream().map(AlbumService::getAlbumDto).toList();
    }

    public AlbumDto getAlbumsById(int id) {
        Album album = albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);
        return getAlbumDto(album);
    }

    public List<AlbumDto> getAlbumsByArtist(int artistId) {
        List<Album> albums = albumRepository.findByArtistsId(artistId);
        return albums.stream().map(AlbumService::getAlbumDto).toList();
    }


    public void createAlbum(int ownerId , CreateAlbumRequest createAlbumRequest) {
        Genre genre = genreRepository.findById(createAlbumRequest.getGenreId()).orElseThrow(GenreNotFoundException::new);
        Set<Artist> artists = createAlbumRequest.getArtistIds().stream().map(artistId -> artistRepository.findById(artistId).orElseThrow(ArtistNotFoundException::new)).collect(Collectors.toSet());
        Set<Track> tracks = createAlbumRequest.getTrackIds().stream().map(trackId -> trackRepository.findById(trackId).orElseThrow(TrackNotFoundException::new)).collect(Collectors.toSet());;
        Set<Track> trackWithAlbum = tracks.stream().filter(track -> track.getAlbum()!=null).collect(Collectors.toSet());
        if (!trackWithAlbum.isEmpty()) throw new MultipleAlbumTrackException(trackWithAlbum.stream().map(TrackService::getTrackDto).toList());

        User owner = userRepository.findById(ownerId).orElseThrow(UserNotFoundException::new);
        Album album = new Album();
        album.setTitle(createAlbumRequest.getTitle());
        album.setCoverUrl(createAlbumRequest.getCoverUrl());
        album.setGenre(genre);
        album.setArtists(artists);
        album.setTracks(tracks);
        tracks.forEach(track ->{
            track.setAlbum(album);
        });
        album.setReleaseDate(new Date());
        album.setOwner(owner);
        albumRepository.save(album);
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

    public void updateAlbum(int id, User requester, CreateAlbumRequest updateAlbumRequest) {
        Album targetAlbum = albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);

        if(requester.getId() != targetAlbum.getOwner().getId() &&  requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedUpdaterException();
        }
        Genre genre = genreRepository.findById(updateAlbumRequest.getGenreId()).orElseThrow(GenreNotFoundException::new);
        Set<Artist> artists = updateAlbumRequest.getArtistIds().stream().map(artistId -> artistRepository.findById(artistId).orElseThrow(ArtistNotFoundException::new)).collect(Collectors.toCollection(HashSet::new));
        Set<Track> tracks = updateAlbumRequest.getTrackIds().stream().map(trackId -> trackRepository.findById(trackId).orElseThrow(TrackNotFoundException::new)).collect(Collectors.toCollection(HashSet::new));
        Set<Track> trackWithAlbum = tracks.stream().filter(track -> track.getAlbum()!=null).collect(Collectors.toCollection(HashSet::new));
        if (!trackWithAlbum.isEmpty()) throw new MultipleAlbumTrackException(trackWithAlbum.stream().map(TrackService::getTrackDto).toList());
        targetAlbum.setTitle(updateAlbumRequest.getTitle());
        targetAlbum.setCoverUrl(updateAlbumRequest.getCoverUrl());
        targetAlbum.setGenre(genre);
        targetAlbum.setArtists(artists);
        targetAlbum.setTracks(tracks);
        tracks.forEach(track ->{
            track.setAlbum(targetAlbum);
        });
        albumRepository.save(targetAlbum);
    }
    @Transactional
    public void deleteAlbum(int id, User requester) {
        Album targetAlbum = albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);

        if(requester.getId() != targetAlbum.getOwner().getId() &&  requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedUpdaterException();
        }

        List<Track> tracks = trackRepository.findByAlbumId(id);
        tracks.forEach(track ->{
            track.setAlbum(null);
            trackRepository.save(track);
        });
        artistRepository.removeAlbumFromArtist(id);
        albumRepository.delete(targetAlbum);
    }

    public void addTrackToAlbum(int id, User requester, AddTrackToAlbumRequest addTrackToAlbumRequest) {
        Album targetAlbum = albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);

        if(requester.getId() != targetAlbum.getOwner().getId() &&  requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedUpdaterException();
        }

        List<Track> tracks = addTrackToAlbumRequest.getTracksId().stream().map(trackId -> trackRepository.findById(trackId).orElseThrow(TrackNotFoundException::new)).collect(Collectors.toCollection(ArrayList::new));
        List<Track> trackWithAlbum = tracks.stream().filter(track -> track.getAlbum()!=null).toList();
        if (!trackWithAlbum.isEmpty()) throw new MultipleAlbumTrackException(trackWithAlbum.stream().map(TrackService::getTrackDto).toList());

        tracks.forEach(track ->{
            track.setAlbum(targetAlbum);
            targetAlbum.getTracks().add(track);
        });

        albumRepository.save(targetAlbum);

    }

    public void deleteTrackFromAlbum(int id, User requester, AddTrackToAlbumRequest addTrackToAlbumRequest) {
        Album targetAlbum = albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);
        if(requester.getId() != targetAlbum.getOwner().getId() &&  requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedUpdaterException();
        }

        List<Track> tracks = addTrackToAlbumRequest.getTracksId().stream().map(trackId -> trackRepository.findById(trackId).orElseThrow(TrackNotFoundException::new)).collect(Collectors.toCollection(ArrayList::new));
        tracks.forEach(track ->{
            track.setAlbum(null);
            targetAlbum.getTracks().remove(track);
        });
        albumRepository.save(targetAlbum);
    }
}
