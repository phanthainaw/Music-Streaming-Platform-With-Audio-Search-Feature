package org.hust.musicstreamingplatform.controller;

import org.hust.musicstreamingplatform.dto.TrackDto;
import org.hust.musicstreamingplatform.model.Track;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/track")
public class TrackController {
    final private TrackDto sampleTrack = TrackDto.builder().id(1).name("Dáng Em").duration("1:20").build();

    @GetMapping("/get")
    public ResponseEntity<TrackDto> getTrack() {
        return ResponseEntity.ok(sampleTrack);
    }

}
