package org.hust.musicstreamingplatform.model;

import jakarta.persistence.*;
import org.apache.catalina.Manager;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String passwordHash;
    private String role;

}
