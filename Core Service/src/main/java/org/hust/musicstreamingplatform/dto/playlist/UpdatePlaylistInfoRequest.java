package org.hust.musicstreamingplatform.dto.playlist;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePlaylistInfoRequest {
    String name;
    String description;
}
