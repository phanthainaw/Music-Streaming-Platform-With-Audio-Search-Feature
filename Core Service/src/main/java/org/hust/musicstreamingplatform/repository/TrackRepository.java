package org.hust.musicstreamingplatform.repository;

import jakarta.transaction.Transactional;
import org.hust.musicstreamingplatform.model.Album;
import org.hust.musicstreamingplatform.model.Artist;
import org.hust.musicstreamingplatform.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {

    List<Track> findByArtistsId(Integer artistId);

    List<Track> findByAlbumId(Integer albumId);

    @Query("SELECT t FROM Track t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Track> searchByTitle(@Param("title") String title);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM track_artist WHERE artist_id = :artistId", nativeQuery = true)
    void removeArtistFromTracks(@Param("artistId") int artistId);
}
