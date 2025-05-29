package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.playlist.AddPlaylistRequest;
import org.hust.musicstreamingplatform.dto.playlist.AddTrackRequest;
import org.hust.musicstreamingplatform.dto.playlist.PlaylistDto;
import org.hust.musicstreamingplatform.dto.playlist.UpdatePlaylistInfoRequest;
import org.hust.musicstreamingplatform.exception.UnauthorizedUpdaterException;
import org.hust.musicstreamingplatform.model.Playlist;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.security.Principal;
import java.util.List;

import static org.hust.musicstreamingplatform.utils.Utils.getUserFromPrincipal;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired private PlaylistService playlistService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PlaylistDto>> getAllPlaylists() {
        List<PlaylistDto> playlistDto = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlistDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> getPlaylistById(@PathVariable int id) {
        PlaylistDto playlistDto = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlistDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlaylistInfo(Principal principal, @PathVariable int id, @RequestBody UpdatePlaylistInfoRequest updatePlaylistInfoRequest) {
        User requestUser = getUserFromPrincipal(principal);
        try {
            playlistService.updatePlaylistInfo(id ,requestUser, updatePlaylistInfoRequest);
        } catch (UnauthorizedUpdaterException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PlaylistDto>> getUserPlaylist(Principal principal ,@PathVariable("id") int id) {
        List<PlaylistDto> playlists = playlistService.getUserPlaylists(id);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/users/me")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER', 'LISTENER')")
    public ResponseEntity<List<PlaylistDto>> getSelfPlaylist(Principal principal) {
        User self = getUserFromPrincipal(principal);
        List<PlaylistDto> playlists = playlistService.getUserPlaylists(self.getId());
        return ResponseEntity.ok(playlists);
    }

    @PostMapping("/{id}/tracks")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER', 'LISTENER')")
    public ResponseEntity<Void> addTracksToPlaylist(Principal principal,@PathVariable int id, @RequestBody AddTrackRequest addTrackRequest) {
        User user = getUserFromPrincipal(principal);
        playlistService.addTracksToPlaylist(id, user,addTrackRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/tracks")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER', 'LISTENER')")
    public ResponseEntity<Void> removeTracksFromPlaylist(Principal principal,@PathVariable int id, @RequestBody AddTrackRequest addTrackRequest) {
        User user = getUserFromPrincipal(principal);
        playlistService.removeTracksFromPlaylist(id, user,addTrackRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER', 'LISTENER')")
    public ResponseEntity<Void> deletePlaylist(Principal principal,@PathVariable int id) {
        User user = getUserFromPrincipal(principal);
        playlistService.deletePlaylist(id, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER', 'LISTENER')")
    public ResponseEntity<Void> addPlaylist(Principal principal,@RequestBody AddPlaylistRequest addPlaylistRequest) {
        User user = getUserFromPrincipal(principal);
        playlistService.addPlaylist(user, addPlaylistRequest);
        return ResponseEntity.ok().build();
    }

}
