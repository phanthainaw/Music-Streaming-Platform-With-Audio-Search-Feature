package org.hust.musicstreamingplatform.dto.playlist;

import lombok.Data;

import java.util.List;

@Data
public class AddTrackRequest {
    List<Integer> tracksId;
}
