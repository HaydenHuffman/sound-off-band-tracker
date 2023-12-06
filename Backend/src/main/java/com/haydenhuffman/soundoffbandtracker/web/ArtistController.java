package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

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
                                  @ModelAttribute Artist artist,
                                  @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String imagePath = StringUtils.cleanPath(imageFile.getOriginalFilename());
            if (imagePath.contains("..")){
                System.out.println("not a valid file");
            }
            artist.setImage(Arrays.toString(Base64.getEncoder().encode(imageFile.getBytes())));

        }
        artistService.save(artist);
        return "redirect:/users/" + userId + "/" + artist.getArtistId();
    }

    @PostMapping("/{userId}/create")
    public String createNewArtist(@PathVariable Long userId, @ModelAttribute Artist artist) throws IOException, InterruptedException {
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
