package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.*;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import com.haydenhuffman.soundoffbandtracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/users")
public class ArtistController {

    private ArtistService artistService;
    private UserService userService;

    public ArtistController(ArtistService artistService, UserService userService) {
        this.artistService = artistService;
        this.userService = userService;
    }

    @GetMapping("/{userId}/{artistId}")
    public String getOneArtist(@PathVariable Long userId, @PathVariable Long artistId, ModelMap model) {
        Artist artist = artistService.findById(artistId);
        Performance performance = new Performance();
        model.put("performance", performance);
        model.put("artist", artist);
        return "artist";
    }

    @PostMapping("/{userId}/{artistId}")
    public String postOneArtist(@PathVariable Long userId, @ModelAttribute Artist artist) {

        artistService.save(artist);
        return "redirect:/users/" + userId;
    }

    @PostMapping("/{userId}/create")
    @ResponseBody
    public String createNewArtist (@PathVariable Long userId, @RequestBody Artist artist) throws IOException, InterruptedException {
        User user = userService.findById(userId);
        artist.setUser(user);
        artistService.createNewArtist(userId, artist);
        return "redirect:/users/" + userId;
    }


    @PostMapping("{userId}/{artistId}/delete")
    public String deleteArtist(@PathVariable Long userId, @PathVariable Long artistId) {
        artistService.delete(artistId);
        return "redirect:/users/" + userId;
    }
}
