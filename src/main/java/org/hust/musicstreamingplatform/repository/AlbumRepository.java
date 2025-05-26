package org.hust.musicstreamingplatform.repository;

import org.hust.musicstreamingplatform.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Optional<Album> findById(String name);

}
