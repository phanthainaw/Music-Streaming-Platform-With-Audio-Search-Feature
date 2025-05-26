package org.hust.musicstreamingplatform.dto.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchTracksResponse {
    private String title;
    private String duration;
    private String artistName;
    private String artistId;
    private String albumName;
    private String albumId;
}
