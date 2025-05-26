package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.search.SearchTracksResponse;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> searchAlbums (@RequestParam String q) {

        return ResponseEntity.ok("");
    }

    @RequestMapping("/tracks")
    public ResponseEntity<SearchTracksResponse> searchTracks (@RequestParam String q) {

        return ResponseEntity.ok(searchService.searchTracks(q));
    }

    @RequestMapping("/artists")
    public ResponseEntity<String> searchArtists (@RequestParam String q) {

        return ResponseEntity.ok("");
    }





}
