package org.hust.musicstreamingplatform.repository;

import org.hust.musicstreamingplatform.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findById(int id);

}
