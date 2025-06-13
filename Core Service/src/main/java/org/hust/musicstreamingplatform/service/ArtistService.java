package org.hust.musicstreamingplatform.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hust.musicstreamingplatform.dto.artist.ArtistDto;
import org.hust.musicstreamingplatform.dto.artist.CreateArtistRequest;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.exception.NoArtistEntityException;
import org.hust.musicstreamingplatform.exception.UnauthorizedUpdaterException;
import org.hust.musicstreamingplatform.exception.album.NoArtistAlbumException;
import org.hust.musicstreamingplatform.exception.artist.ArtistNotFoundException;
import org.hust.musicstreamingplatform.exception.track.NoArtistTrackException;
import org.hust.musicstreamingplatform.model.Artist;
import org.hust.musicstreamingplatform.model.User;
import org.hust.musicstreamingplatform.model.enums.Role;
import org.hust.musicstreamingplatform.repository.ArtistRepository;
import org.hust.musicstreamingplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private TrackService trackService;
    @Autowired
    private AlbumService albumService;

    public List<ArtistDto> getAllArtists() {
        List<Artist> artists = artistRepository.findAll();
        return artists.stream().map(ArtistService::getArtistDto).toList();
    }

    public List<ArtistDto> searchByName(String name) {
        List<Artist> artists = artistRepository.searchByName(name);
        return artists.stream().map(artist -> ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .bio(artist.getBio())
                .country(artist.getCountry())
                .avatarUrl(artist.getAvatarUrl())
                .build()).toList();
    }

    public ArtistDto getArtistById(int id) {
        Artist artist = artistRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
        return getArtistDto(artist);
    }

    private static ArtistDto getArtistDto(Artist artist){
        return ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .bio(artist.getBio())
                .avatarUrl(artist.getAvatarUrl())
                .country(artist.getCountry())
                .build();
    }


    public void createArtist(User manager, CreateArtistRequest createArtistRequest) {
        Artist artist = Artist.builder()
                .name(createArtistRequest.getName())
                .bio(createArtistRequest.getBio())
                .country(createArtistRequest.getCountry())
                .avatarUrl(createArtistRequest.getAvatarUrl())
                .manager(manager)
                .build();
        artistRepository.save(artist);
    }

    public void updateArtistInfo(int id, User requester ,CreateArtistRequest updateArtistRequest) {
        Artist targetArtist = artistRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
        if(requester.getId() != targetArtist.getManager().getId()) {
            throw new UnauthorizedUpdaterException();
        }
        targetArtist.setName(updateArtistRequest.getName());
        targetArtist.setBio(updateArtistRequest.getBio());
        targetArtist.setCountry(updateArtistRequest.getCountry());
        targetArtist.setAvatarUrl(updateArtistRequest.getAvatarUrl());
        artistRepository.save(targetArtist);
    }

    @Transactional
    public void deleteArtist(int id, User requester) {
        Artist targetArtist = artistRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
        if(requester.getId() != targetArtist.getManager().getId()&&requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedUpdaterException();
        }
        NoArtistTrackException noArtistTrackException = null;
        NoArtistAlbumException noArtistAlbumException = null;
        boolean exceptionFlg = false;
        try {
            trackService.removeArtistFromTracks(id);
        } catch (NoArtistTrackException e) {
           noArtistTrackException = e;
           exceptionFlg = true;
        }

        try {
            albumService.removeArtistFromAlbum(id);
        } catch (NoArtistAlbumException e) {
            noArtistAlbumException = e;
            exceptionFlg = true;
        }
        if (exceptionFlg) {
            NoArtistEntityException exception = new NoArtistEntityException();
            if(noArtistAlbumException!=null)exception.setNoArtistAlbums(noArtistAlbumException.NoArtistAlbums);
            if(noArtistTrackException!=null) exception.setNoArtistTracks(noArtistTrackException.NoArtistTracks);
            throw exception;
        }
        artistRepository.delete(targetArtist);
    }
}
