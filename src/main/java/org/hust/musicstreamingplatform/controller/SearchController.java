package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.album.AlbumDto;
import org.hust.musicstreamingplatform.dto.artist.ArtistDto;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/search")

public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/")
    public ResponseEntity<String> searchAll (@RequestParam String q) {

        return ResponseEntity.ok("");
    }

    @RequestMapping("/albums")
    public ResponseEntity<List<AlbumDto>> searchAlbums (@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchAlbums(q));
    }

    @RequestMapping("/tracks")
    public ResponseEntity<List<TrackDto>> searchTracks (@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchTracks(q));
    }

    @RequestMapping("/artists")
    public ResponseEntity<List<ArtistDto>> searchArtists (Principal principal,@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchArtists(q));
    }





}
