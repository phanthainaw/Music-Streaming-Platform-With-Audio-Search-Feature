package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.dto.track.UpdateTrackRequest;
import org.hust.musicstreamingplatform.dto.track.UploadTrackRequest;
import org.hust.musicstreamingplatform.model.Track;
import org.hust.musicstreamingplatform.model.enums.Role;
import org.hust.musicstreamingplatform.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.hust.musicstreamingplatform.utils.Utils.getUserFromPrincipal;

@RestController
@RequestMapping("/tracks")

public class TrackController {

    @Autowired
    private TrackService trackService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrackDto>> getAllTracks() {
        List<TrackDto> tracks = trackService.getAllTracks();
        return ResponseEntity.status(HttpStatus.OK).body(tracks);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('PUBLISHER', 'ADMIN')")
    public ResponseEntity<Void> uploadTrack(Principal principal, @RequestBody UploadTrackRequest uploadTrackRequest) {
        trackService.uploadTrack(getUserFromPrincipal(principal), uploadTrackRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTrack(Principal principal, @PathVariable int id, @RequestBody UpdateTrackRequest updateTrackRequest) {
        if(getUserFromPrincipal(principal).getId()==updateTrackRequest.getPublisherId()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        trackService.updateTrack(id, updateTrackRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PUBLISHER','ADMIN')")
    public ResponseEntity<Void> deleteTrack(Principal principal, @PathVariable int id) {
        if(getUserFromPrincipal(principal).getRole()== Role.PUBLISHER && getUserFromPrincipal(principal).getId()!=id) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        trackService.deleteTrack(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<List<TrackDto>> getTracksByArtist(@PathVariable int id) {
        List<TrackDto> trackDtos =  trackService.getTracksByArtist(id);
        return ResponseEntity.ok(trackDtos);
    }

    @GetMapping("/album/{id}")
    public ResponseEntity<List<TrackDto>> getTracksByAlbum(@PathVariable int id) {
        List<TrackDto> trackDtos =  trackService.getTracksByAlbum(id);
        return ResponseEntity.ok(trackDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDto> getTrack(@PathVariable int id) {
        TrackDto getTrack = trackService.getTrack(id);
        return ResponseEntity.ok(getTrack);
    }
}
