package org.hust.musicstreamingplatform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.Manager;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String bio;
    private String avatarUrl;
    private String country;

    @ManyToOne
    @JoinColumn(name="manager_id")
    private User manager;

    @ManyToMany(mappedBy = "artists")
    private List<Track> tracks;

    @ManyToMany(mappedBy = "artists")
    private List<Album> albums;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist artist)) return false;
        return id == artist.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
