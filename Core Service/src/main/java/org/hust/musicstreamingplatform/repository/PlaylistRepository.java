package org.hust.musicstreamingplatform.repository;

import org.hust.musicstreamingplatform.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    Optional<Playlist> findById(int id);


    List<Playlist> findByOwnerId(int id);
}
