package org.hust.musicstreamingplatform.dto.album;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateAlbumRequest {
    String title;
    String coverUrl;
    int genreId;
    List<Integer> artistIds;
    List<Integer> trackIds;
}
