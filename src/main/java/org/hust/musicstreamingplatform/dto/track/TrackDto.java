package org.hust.musicstreamingplatform.dto.track;

import lombok.*;
import org.hust.musicstreamingplatform.model.Genre;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TrackDto {
    private int id;
    private String title;
    private Float duration;
    private List<String> artistsName;
    private List<Integer> artistsId;
    private String albumName;
    private int albumId;
}
