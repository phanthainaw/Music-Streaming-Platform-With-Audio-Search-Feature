package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.artist.ArtistDto;
import org.hust.musicstreamingplatform.dto.artist.CreateArtistRequest;
import org.hust.musicstreamingplatform.dto.artist.DeleteArtistResponse;
import org.hust.musicstreamingplatform.exception.NoArtistEntityException;
import org.hust.musicstreamingplatform.exception.UnauthorizedUpdaterException;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.service.ArtistService;
import org.hust.musicstreamingplatform.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.hust.musicstreamingplatform.utils.Utils.getUserFromPrincipal;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;
    @Autowired
    private PlaylistService playlistService;


    @GetMapping("")
    public ResponseEntity<List<ArtistDto>> getAllArtists() {
        List<ArtistDto> artistDtos =  artistService.getAllArtists();
        return ResponseEntity.ok(artistDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable("id") int id) {
        ArtistDto artistDto = artistService.getArtistById(id);
        return ResponseEntity.ok(artistDto);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER')")
    public ResponseEntity<ArtistDto> createArtist(Principal principal,@RequestBody CreateArtistRequest createArtistRequest) {
        User manager = getUserFromPrincipal(principal);
        artistService.createArtist(manager, createArtistRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER')")
    public ResponseEntity<Void> updateArtist(Principal principal,@RequestBody CreateArtistRequest putArtistRequest, @PathVariable("id") int id) {
        User requester = getUserFromPrincipal(principal);
        try {
            artistService.updateArtistInfo(id,requester, putArtistRequest);
        } catch (UnauthorizedUpdaterException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUBLISHER')")
    public ResponseEntity<DeleteArtistResponse> deleteArtist(Principal principal, @PathVariable("id") int id) {
        User requester = getUserFromPrincipal(principal);
        try {
            artistService.deleteArtist(id, requester);
        } catch (NoArtistEntityException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(DeleteArtistResponse.builder()
                    .toDeleteTracks(e.getNoArtistTracks())
                    .toDeleteAlbums(e.getNoArtistAlbums())
                    .build());
        }
        return ResponseEntity.ok().build();
    }

}
