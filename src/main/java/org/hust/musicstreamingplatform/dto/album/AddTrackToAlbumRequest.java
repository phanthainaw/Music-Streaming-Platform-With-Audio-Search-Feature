package org.hust.musicstreamingplatform.dto.album;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddTrackToAlbumRequest {
    List<Integer> tracksId;
}
