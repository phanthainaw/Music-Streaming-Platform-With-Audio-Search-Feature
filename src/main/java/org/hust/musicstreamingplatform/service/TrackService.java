package org.hust.musicstreamingplatform.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.dto.track.UpdateTrackRequest;
import org.hust.musicstreamingplatform.dto.track.UploadTrackRequest;
import org.hust.musicstreamingplatform.exception.album.AlbumNotFoundException;
import org.hust.musicstreamingplatform.exception.artist.ArtistNotFoundException;
import org.hust.musicstreamingplatform.exception.track.NoArtistTrackException;
import org.hust.musicstreamingplatform.exception.genre.GenreNotFoundException;
import org.hust.musicstreamingplatform.exception.track.TrackNotFoundException;
import org.hust.musicstreamingplatform.model.*;
import org.hust.musicstreamingplatform.repository.AlbumRepository;
import org.hust.musicstreamingplatform.repository.ArtistRepository;
import org.hust.musicstreamingplatform.repository.GenreRepository;
import org.hust.musicstreamingplatform.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final ArtistRepository artistRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<TrackDto> searchByTitle(String title) {
        List<Track> tracks = trackRepository.searchByTitle(title);
        return tracks.stream().map(TrackService::getTrackDto).toList();
    }

    public void uploadTrack(User publisher, UploadTrackRequest uploadTrackRequest) {
        Genre genre = genreRepository.findById(uploadTrackRequest.getGenreId()).orElseThrow(GenreNotFoundException::new);
        Album album = albumRepository.findById(uploadTrackRequest.getAlbumId()).orElseThrow(AlbumNotFoundException::new);
        Set<Artist> artists = uploadTrackRequest.getArtistIds().stream().map(artist -> artistRepository.findById(artist).orElseThrow(ArtistNotFoundException::new)).collect(Collectors.toCollection(HashSet::new));
        Track track = Track.builder().title(uploadTrackRequest.getTitle()).duration(uploadTrackRequest.getDuration()).genre(genre).album(album).artists(artists).coverUrl(uploadTrackRequest.getCoverUrl()).publisher(publisher).releaseDate(new Date()).build();
        trackRepository.save(track);
    }

    public void updateTrack(int id, UpdateTrackRequest updateTrackRequest) {
        Track track = trackRepository.findById(id).orElseThrow(TrackNotFoundException::new);
        Genre genre = genreRepository.findById(updateTrackRequest.getGenreId()).orElseThrow(GenreNotFoundException::new);
        Album album = albumRepository.findById(updateTrackRequest.getAlbumId()).orElseThrow(AlbumNotFoundException::new);
        Set<Artist> artists = updateTrackRequest.getArtistIds().stream().map(artist -> artistRepository.findById(artist).orElseThrow(ArtistNotFoundException::new)).collect(Collectors.toCollection(HashSet::new));
        track.setTitle(updateTrackRequest.getTitle());
        track.setArtists(artists);
        track.setAlbum(album);
        track.setGenre(genre);
        track.setCoverUrl(updateTrackRequest.getCoverUrl());
        trackRepository.save(track);
    }

    public void deleteTrack(int id) {
        Track track = trackRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
        trackRepository.delete(track);
    }

    public List<TrackDto> getTracksByArtist(int artistId) {
        List<Track> tracks = trackRepository.findByArtistsId(artistId);
        return tracks.stream().map(TrackService::getTrackDto).toList();
    }

    public List<TrackDto> getTracksByAlbum(int artistId) {
        List<Track> tracks = trackRepository.findByAlbumId(artistId);
        return tracks.stream().map(TrackService::getTrackDto).toList();
    }

    public TrackDto getTrack (int trackId) {
        Track track = trackRepository.findById(trackId).orElseThrow(TrackNotFoundException::new);
        return getTrackDto(track);
    }

    public static TrackDto getTrackDto(Track track) {
            String albumName = track.getAlbum().getTitle();
            int albumId = track.getAlbum().getId();
            List<String> artistName = track.getArtists().stream().map(Artist::getName).toList();
            List<Integer> artistIds = track.getArtists().stream().map(Artist::getId).toList();
            return TrackDto.builder()
                    .id(track.getId())
                    .title(track.getTitle())
                    .duration(track.getDuration())
                    .albumId(albumId)
                    .albumName(albumName)
                    .artistsId(artistIds)
                    .artistsName(artistName)
                    .genre(track.getGenre().getTitle())
                    .coverUrl(track.getCoverUrl())
                    .build();}

    public List<TrackDto> getAllTracks() {
        List<Track> tracks = trackRepository.findAll();
        return tracks.stream().map(TrackService::getTrackDto).toList();
    }

    @Transactional
    public void removeArtistFromTracks(int artistId) {
        List<Track> tracks = trackRepository.findByArtistsId(artistId);

        // Identify tracks where the artist is the only one
        List<Track> soloTracks = tracks.stream()
                .filter(track -> track.getArtists().size() == 1)
                .toList();

        if (!soloTracks.isEmpty()) {
            // Throw an exception with the IDs of the problematic tracks
            throw new NoArtistTrackException(soloTracks.stream()
                    .map(TrackService::getTrackDto).toList());
        }

        // Proceed to remove artist from all other tracks
        trackRepository.removeArtistFromTracks(artistId);
        tracks.forEach(track -> entityManager.refresh(track));
    }

}

