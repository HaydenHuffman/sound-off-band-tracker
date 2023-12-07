package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import com.haydenhuffman.soundoffbandtracker.util.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    private UserServiceImpl userService;
    private ArtistService artistService;
    private SecurityUtils securityUtils;


    public UserController(UserServiceImpl userService, ArtistService artistService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.artistService = artistService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/register")
    public String getCreateUser(ModelMap model) {
        model.put("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postCreateUser(User user, ModelMap model) {
        userService.createUser(user);
        model.addAttribute("userId", user.getUserId());
        return "redirect:/{userId}";
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("@securityUtils.isUserIdMatch(#userId)")
    public String getUserById(@PathVariable Long userId, ModelMap model) {
         User user = userService.findById(userId);
         model.put("user", user);
         return "user";
    }

    @GetMapping("/users/{userId}/top-artists")
    public String getUsersTopArtists(@PathVariable Long userId, Model model) {
        List<Artist> topArtists = artistService.findTopArtists(userId);
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("topArtists", topArtists);
        return "top-artists";
    }

    @GetMapping("/home/{userId}")
    public String getHomePage(ModelMap model, @PathVariable Long userId) {
//        model.put("artist", artist);
        return null;
    }
}
