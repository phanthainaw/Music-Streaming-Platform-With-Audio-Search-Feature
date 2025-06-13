package org.hust.musicstreamingplatform.dto.playlist;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.dto.track.TrackDto;

import java.util.List;

@Data
@Builder
public class PlaylistDto {
    int id;
    String name;
    String description;
    List<TrackDto> tracks;
    int ownerId;
    String ownerName;
}
