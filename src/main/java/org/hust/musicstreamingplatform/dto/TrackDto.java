package org.hust.musicstreamingplatform.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TrackDto {
    private int id;
    private String name;
    private String duration;
}
