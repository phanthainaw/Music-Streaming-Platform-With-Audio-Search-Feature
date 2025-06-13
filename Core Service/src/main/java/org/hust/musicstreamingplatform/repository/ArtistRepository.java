package org.hust.musicstreamingplatform.repository;

import jakarta.transaction.Transactional;
import org.hust.musicstreamingplatform.model.Album;
import org.hust.musicstreamingplatform.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    @Override
    Optional<Artist> findById(Integer integer);

    @Query("SELECT a FROM Artist a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Artist> searchByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM album_artist WHERE album_id = :albumId", nativeQuery = true)
    void removeAlbumFromArtist(@Param("albumId") int albumId);

    List<Artist> findByAlbumsId(Integer id);
}
