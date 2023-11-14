package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistService {
    private ArtistRepository artistRepository;
    private UserService userService;
    public ArtistService(ArtistRepository artistRepository, UserService userService) {
        this.artistRepository = artistRepository;
        this.userService = userService;
    }
    public Artist createNewArtist(Long userId, Artist artist) {
        User user = userService.findById(userId);
        user.getArtists().add(artist);
        artist.setUser(user);
        artist.setImage("/images/band.jpg");
        artistRepository.save(artist);
        return artist;

    }

    public Artist findById(Long artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        return artistOpt.orElse(new Artist());
    }

    public void save(Artist artist) {
        artistRepository.save(artist);
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }
}
