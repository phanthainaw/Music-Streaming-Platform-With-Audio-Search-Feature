package org.hust.musicstreamingplatform.dto.album;

import lombok.Builder;
import lombok.Data;
import org.hust.musicstreamingplatform.model.Genre;
import org.hust.musicstreamingplatform.model.enums.GenreEnum;

import java.util.Date;

@Data
@Builder
public class AlbumDto {
    int id;
    String title;
    String coverUrl;
    Date releaseDate;
    GenreEnum genre;
}
