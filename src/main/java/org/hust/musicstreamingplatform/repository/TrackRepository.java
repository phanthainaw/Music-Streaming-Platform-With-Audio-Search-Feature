package org.hust.musicstreamingplatform.repository;

import org.hust.musicstreamingplatform.model.Album;
import org.hust.musicstreamingplatform.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {

    @Query("SELECT t FROM Track t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Track> searchByTitle(@Param("title") String title);

}
