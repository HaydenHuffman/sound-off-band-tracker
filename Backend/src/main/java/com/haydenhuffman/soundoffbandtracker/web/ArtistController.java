package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/users")
public class ArtistController {

    private ArtistService artistService;
    private UserServiceImpl userService;

    public ArtistController(ArtistService artistService, UserServiceImpl userService) {
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
    public String updateOneArtist(@PathVariable Long userId,
                                  @ModelAttribute Artist artist) throws IOException {


        artistService.save(artist);
        return "redirect:/users/" + userId + "/" + artist.getArtistId();
    }

    @PostMapping("/{userId}/create")
    public String createNewArtist(@PathVariable Long userId, @ModelAttribute Artist artist) throws IOException, InterruptedException {
        User user = userService.findById(userId);
        artist.setUser(user);
        artistService.createNewArtist(user, artist);
        return "redirect:/users/" + userId;
    }


    @PostMapping("{userId}/{artistId}/delete")
    public String deleteArtist(@PathVariable Long userId, @PathVariable Long artistId) {
        artistService.delete(artistId);
        return "redirect:/users/" + userId;
    }
}
