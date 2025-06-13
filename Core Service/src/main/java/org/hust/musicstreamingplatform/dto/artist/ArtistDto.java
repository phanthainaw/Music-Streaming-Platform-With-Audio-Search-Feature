package org.hust.musicstreamingplatform.dto.artist;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtistDto {
    int id;
    String name;
    String bio;
    String avatarUrl;
    String country;

}
