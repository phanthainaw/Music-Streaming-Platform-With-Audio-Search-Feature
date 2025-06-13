package org.hust.musicstreamingplatform.dto.playlist;
import lombok.Data;

import java.util.List;

@Data
public class AddPlaylistRequest {
    String name;
    String description;
    List<Integer> tracksId;
}
