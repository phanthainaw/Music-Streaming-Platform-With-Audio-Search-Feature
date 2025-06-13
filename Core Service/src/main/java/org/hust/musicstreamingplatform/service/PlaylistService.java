package org.hust.musicstreamingplatform.service;

import org.hust.musicstreamingplatform.dto.playlist.AddPlaylistRequest;
import org.hust.musicstreamingplatform.dto.playlist.AddTrackRequest;
import org.hust.musicstreamingplatform.dto.playlist.PlaylistDto;
import org.hust.musicstreamingplatform.dto.playlist.UpdatePlaylistInfoRequest;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.exception.UnauthorizedUpdaterException;
import org.hust.musicstreamingplatform.exception.playlist.PlaylistNotFoundException;
import org.hust.musicstreamingplatform.exception.track.TrackNotFoundException;
import org.hust.musicstreamingplatform.model.Playlist;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.repository.PlaylistRepository;
import org.hust.musicstreamingplatform.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    @Autowired private PlaylistRepository playlistRepository;
    @Autowired private TrackRepository trackRepository;

    public List<PlaylistDto> getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        return playlists.stream().map(PlaylistService::getPlaylistDto).toList();
    }

    public PlaylistDto getPlaylistById(int id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(PlaylistNotFoundException::new);
        return getPlaylistDto(playlist);
    }

    public static PlaylistDto getPlaylistDto(Playlist playlist ) {
        User owner = playlist.getOwner();
        List<TrackDto> tracks= playlist.getTracks().stream()
                .map(TrackService::getTrackDto).collect(Collectors.toList());
        return PlaylistDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .ownerId(owner.getId())
                .ownerName(owner.getName())
                .tracks(tracks)
                .build();
    }

    public void updatePlaylistInfo(int id, User requestUser, UpdatePlaylistInfoRequest updatePlaylistInfoRequest) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(PlaylistNotFoundException::new);
        if (!playlist.getOwner().equals(requestUser)) throw new UnauthorizedUpdaterException();
        playlist.setName(updatePlaylistInfoRequest.getName());
        playlist.setDescription(updatePlaylistInfoRequest.getDescription());
        playlistRepository.save(playlist);
    }

    public List<PlaylistDto> getUserPlaylists(int id) {
        List<Playlist> playlists = playlistRepository.findByOwnerId(id);
        return playlists.stream().map(PlaylistService::getPlaylistDto).toList();
    }

    public void addTracksToPlaylist(int id, User user,AddTrackRequest addTrackRequest) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(PlaylistNotFoundException::new);

        if (!playlist.getOwner().equals(user)) throw new UnauthorizedUpdaterException();

        List<Track> tracks = addTrackRequest.getTracksId().stream().map(
                track -> trackRepository.findById(track)
                        .orElseThrow(TrackNotFoundException::new))
                .collect(Collectors.toList());
        tracks.forEach(track -> {
            playlist.getTracks().add(track);
        });
        playlistRepository.save(playlist);
    }
    public void removeTracksFromPlaylist(int id, User user,AddTrackRequest addTrackRequest) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(PlaylistNotFoundException::new);

        if (!playlist.getOwner().equals(user)) throw new UnauthorizedUpdaterException();

        List<Track> tracks = addTrackRequest.getTracksId().stream().map(
                        track -> trackRepository.findById(track)
                                .orElseThrow(TrackNotFoundException::new))
                .collect(Collectors.toList());
        tracks.forEach(track -> {
            playlist.getTracks().remove(track);
        });
        playlistRepository.save(playlist);
    }

    public void deletePlaylist(int id, User user) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(PlaylistNotFoundException::new);
        if (!playlist.getOwner().equals(user)) throw new UnauthorizedUpdaterException();
        playlistRepository.delete(playlist);
    }


    public void addPlaylist(User user, AddPlaylistRequest addPlaylistRequest) {
        List<Track> tracks = addPlaylistRequest.getTracksId().stream().map(
                        track -> trackRepository.findById(track)
                                .orElseThrow(TrackNotFoundException::new))
                .collect(Collectors.toList());
        Playlist playlist = new Playlist();
        playlist.setOwner(user);
        playlist.setName(addPlaylistRequest.getName());
        playlist.setDescription(addPlaylistRequest.getDescription());
        playlist.setTracks(tracks);
        playlistRepository.save(playlist);
    }
}
