package org.hust.musicstreamingplatform.dto.track;

import lombok.Data;

import java.util.List;

@Data
public class UploadTrackRequest {
    private String title;
    private Float duration;
    private List<Integer> artistIds;
    private int albumId;
    private int genreId;
    private String coverUrl;
}
