package org.hust.musicstreamingplatform.dto.artist;

import lombok.Data;

@Data
public class CreateArtistRequest {
    private String name;
    private String bio;
    private String avatarUrl;
    private String country;
}
