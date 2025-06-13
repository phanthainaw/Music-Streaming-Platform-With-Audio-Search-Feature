package org.hust.musicstreamingplatform.dto.album;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AlbumDto {
    int id;
    String title;
    String coverUrl;
    Date releaseDate;
    String genre;
}
