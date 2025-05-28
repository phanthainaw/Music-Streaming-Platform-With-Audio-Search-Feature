package org.hust.musicstreamingplatform.service;

import org.hust.musicstreamingplatform.dto.artist.ArtistDto;
import org.hust.musicstreamingplatform.dto.artist.CreateArtistRequest;
import org.hust.musicstreamingplatform.dto.track.TrackDto;
import org.hust.musicstreamingplatform.exception.UnauthorizedUpdaterException;
import org.hust.musicstreamingplatform.exception.artist.ArtistNotFoundException;
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
    private TrackService trackService;

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

    public void deleteArtist(int id, User requester) {
        Artist targetArtist = artistRepository.findById(id).orElseThrow(ArtistNotFoundException::new);
        List<TrackDto> tracksIncludeTargetArtist = trackService.getTracksByArtist(id) ;

        for(TrackDto trackDto : tracksIncludeTargetArtist) {

        }


        if(requester.getId() != targetArtist.getManager().getId()&requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedUpdaterException();
        }
        artistRepository.delete(targetArtist);
    }
}
