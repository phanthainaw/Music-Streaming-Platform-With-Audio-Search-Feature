package org.hust.musicstreamingplatform.dto.track;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTrackRequest {
    private String title;
    private List<Integer> artistIds;
    private int albumId;
    private int genreId;
    private String coverUrl;
    private int publisherId;
}


