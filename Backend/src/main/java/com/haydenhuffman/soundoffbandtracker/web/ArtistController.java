package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/users/{userId}")
public class ArtistController {

    private ArtistService artistService;
    private UserServiceImpl userService;

    public ArtistController(ArtistService artistService, UserServiceImpl userService) {
        this.artistService = artistService;
        this.userService = userService;
    }

    @GetMapping("/{artistId}")
    public String getOneArtist(@PathVariable Long userId, @PathVariable Long artistId, ModelMap model) {
        Artist artist = artistService.findById(artistId);
        Performance performance = new Performance();
        model.put("performance", performance);
        model.put("artist", artist);
        return "artist";
    }

    @PostMapping("/{artistId}")
    public String updateOneArtist(@PathVariable Long userId,
                                  @ModelAttribute Artist artist) throws IOException {

        User user = userService.findById(userId);
        artistService.save(user, artist);
        return "redirect:/users/" + userId + "/" + artist.getArtistId();
    }

    @GetMapping("/create")
    public String getCreateNewArtist(Model model, @PathVariable Long userId) {
        model.addAttribute("artist", new Artist());
        User currentUser = userService.findById(userId);
        model.addAttribute("user", currentUser);
        return "create-artist";
    }

    @PostMapping("/create")
    public String createNewArtist(@PathVariable Long userId, @ModelAttribute Artist artist) throws IOException, InterruptedException {
        User user = userService.findById(userId);
        artistService.createNewArtist(user, artist);
        return "redirect:/users/" + userId;
    }


    @PostMapping("/{artistId}/delete")
    public String deleteArtist(@PathVariable Long userId, @PathVariable Long artistId) {
        User user = userService.findById(userId);
        Artist artist = artistService.findById(artistId);
        artist.getPerformances().clear();
        artistService.save(user, artist);
        artistService.delete(artistId);
        return "redirect:/users/" + userId;
    }
}
