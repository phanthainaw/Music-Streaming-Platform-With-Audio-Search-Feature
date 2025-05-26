package org.hust.musicstreamingplatform.dto.search;

import lombok.Builder;
import lombok.Data;
import org.hust.musicstreamingplatform.dto.track.TrackDto;

import java.util.List;

@Data
@Builder
public class SearchTracksResponse {
    private List<TrackDto> tracks;
}
