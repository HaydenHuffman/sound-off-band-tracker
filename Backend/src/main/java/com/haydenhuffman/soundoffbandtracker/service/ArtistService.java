package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.ArtistRepository;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {
    private ArtistRepository artistRepository;
    private UserService userService;
    public ArtistService(ArtistRepository artistRepository, UserService userService) {
        this.artistRepository = artistRepository;
        this.userService = userService;
    }
    public Artist createNewArtist(Long userId) {
        Artist artist = new Artist();
        User user = userService.findById(userId);
        user.getArtists().add(artist);
        artist.setUser(user);
        artist.setName("Artist #" + user.getArtists().size());
        artistRepository.save(artist);
        return artist;

    }
}
