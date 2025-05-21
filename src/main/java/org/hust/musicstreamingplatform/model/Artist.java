package org.hust.musicstreamingplatform.model;

import jakarta.persistence.*;
import org.apache.catalina.Manager;

@Entity
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
