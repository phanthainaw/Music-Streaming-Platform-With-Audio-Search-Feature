package org.hust.musicstreamingplatform.dto.track;

import lombok.Data;

@Data
public class UploadTrackRequest {
    private String name;
    private Float duration;
    private int albumId;
    private int genreId;
    private String coverUrl;
    private String cdnUrl;
}
