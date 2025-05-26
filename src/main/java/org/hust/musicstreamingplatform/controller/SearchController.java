package org.hust.musicstreamingplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")

public class SearchController {
    @RequestMapping("/")
    public ResponseEntity<String> searchAll (@RequestParam String q) {

        return ResponseEntity.ok("");
    }

    @RequestMapping("/albums")
    public ResponseEntity<String> searchAlbums (@RequestParam String q) {

        return ResponseEntity.ok("");
    }

    @RequestMapping("/tracks")
    public ResponseEntity<String> searchTracks (@RequestParam String q) {

        return ResponseEntity.ok("");
    }

    @RequestMapping("/artists")
    public ResponseEntity<String> searchArtists (@RequestParam String q) {

        return ResponseEntity.ok("");
    }





}
