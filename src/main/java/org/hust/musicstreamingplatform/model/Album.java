package org.hust.musicstreamingplatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Data
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String coverUrl;

    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "album", cascade = CascadeType.PERSIST)
    private Set<Track> tracks;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name="album_artist",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )

    private Set<Artist> artists;


}
