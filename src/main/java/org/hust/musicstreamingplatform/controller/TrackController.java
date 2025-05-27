package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.dto.track.UploadTrackRequest;
import org.hust.musicstreamingplatform.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @PostMapping("")
    public ResponseEntity<Void> createTrack(@RequestBody UploadTrackRequest uploadTrackRequest) {



        return ResponseEntity.ok().build();
    }

}
