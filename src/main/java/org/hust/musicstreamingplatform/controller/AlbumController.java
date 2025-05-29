package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.album.AddTrackToAlbumRequest;
import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.dto.album.CreateAlbumRequest;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.exception.album.MultipleAlbumTrackException;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.hust.musicstreamingplatform.utils.Utils.getUserFromPrincipal;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping("")
    public ResponseEntity<List<AlbumDto>> getAllAlbums() {
        List<AlbumDto> albumDtos = albumService.getAllAlbums();
        return ResponseEntity.ok().body(albumDtos) ;
    }

    @GetMapping("{id}")
    public ResponseEntity<AlbumDto> getAlbumById(@PathVariable int id) {
        AlbumDto albumDto = albumService.getAlbumsById(id);
        return ResponseEntity.ok().body(albumDto);

    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<List<AlbumDto>> getAlbumsByArtist(@PathVariable int id) {
        List<AlbumDto> albumDtos = albumService.getAlbumsByArtist(id);
        return ResponseEntity.ok().body(albumDtos);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','PUBLISHER')")
    public ResponseEntity<List<TrackDto>> createAlbum(Principal principal, @RequestBody CreateAlbumRequest createAlbumRequest) {
        int ownerId = getUserFromPrincipal(principal).getId();
        try {
            albumService.createAlbum(ownerId,createAlbumRequest);
        } catch (MultipleAlbumTrackException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.TrackWithAlbum);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PUBLISHER')")
    public ResponseEntity<List<TrackDto>> updateAlbum(Principal principal , @PathVariable int id, @RequestBody CreateAlbumRequest updateAlbumRequest) {
        User requester = getUserFromPrincipal(principal);

        try {
            albumService.updateAlbum(id, requester, updateAlbumRequest);
        } catch (MultipleAlbumTrackException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.TrackWithAlbum);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PUBLISHER')")
    public ResponseEntity<Void> deleteAlbum(Principal principal, @PathVariable int id) {
        User requester = getUserFromPrincipal(principal);
        albumService.deleteAlbum(id, requester);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping ("/{id}/tracks")
    @PreAuthorize("hasAnyAuthority('ADMIN','PUBLISHER')")
    public ResponseEntity<Void> addTrackToAlbum(Principal principal,@PathVariable int id, @RequestBody AddTrackToAlbumRequest addTrackToAlbumRequest) {
        User requester = getUserFromPrincipal(principal);
        albumService.addTrackToAlbum(id, requester, addTrackToAlbumRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping ("/{id}/tracks")
    @PreAuthorize("hasAnyAuthority('ADMIN','PUBLISHER')")
    public ResponseEntity<Void> deleteTrackFromAlbum(Principal principal,@PathVariable int id, @RequestBody AddTrackToAlbumRequest addTrackToAlbumRequest) {
        User requester = getUserFromPrincipal(principal);
        albumService.deleteTrackFromAlbum(id, requester, addTrackToAlbumRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
