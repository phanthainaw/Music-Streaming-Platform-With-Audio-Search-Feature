package org.hust.musicstreamingplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.Manager;

@Entity
@Data
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

}
