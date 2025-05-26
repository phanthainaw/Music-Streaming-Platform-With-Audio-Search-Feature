package org.hust.musicstreamingplatform.repository;

import org.hust.musicstreamingplatform.model.Album;
import org.hust.musicstreamingplatform.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {

    List<Track> searchTracksByTitle(String title);

}
