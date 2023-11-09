package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArtistController {

    private ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }
    @PostMapping("users/{userId}/create")
    public String createNewArtist (@PathVariable Long userId) {
        Artist artist = artistService.createNewArtist(userId);
        return "redirect:/users/" + userId;
    }
}
