package org.hust.musicstreamingplatform.repository;

import jakarta.transaction.Transactional;
import org.hust.musicstreamingplatform.model.Album;
import org.hust.musicstreamingplatform.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Optional<Album> findById(int id);

    List<Album> findByArtistsId(Integer artistId);

    Optional<Album> findByTitle(String title);

    @Query("SELECT a FROM Album a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Album> searchByTitle(@Param("title") String title);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM album_artist WHERE artist_id = :artistId", nativeQuery = true)
    void removeArtistFromAlbums(@Param("artistId") int artistId);



}
