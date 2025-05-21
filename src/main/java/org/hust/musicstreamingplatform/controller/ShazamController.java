package org.hust.musicstreamingplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShazamController {
    @GetMapping("/")
    public String matchSong(){
        return "index";
    }

}
