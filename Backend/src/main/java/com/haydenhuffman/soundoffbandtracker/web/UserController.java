package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.dao.request.SignUpRequest;
import com.haydenhuffman.soundoffbandtracker.dao.response.JwtAuthenticationResponse;
import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.security.AuthenticationServiceImpl;
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
import java.util.Optional;

@Controller
public class UserController {

    private UserServiceImpl userService;
    private ArtistService artistService;
    private SecurityUtils securityUtils;
    private AuthenticationServiceImpl authenticationService;


    public UserController(UserServiceImpl userService, ArtistService artistService, SecurityUtils securityUtils, AuthenticationServiceImpl authenticationService) {
        this.userService = userService;
        this.artistService = artistService;
        this.securityUtils = securityUtils;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String getIndex() {
        return "index";
    }
    @GetMapping("/register")
    public String getCreateUser(ModelMap model) {
        model.put("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postCreateUser(ModelMap model, SignUpRequest request) {
        Optional<User> existingUser = userService.findUserByEmail(request.email());
        if (existingUser.isEmpty()) {
            JwtAuthenticationResponse signupResponse = authenticationService.signup(request);
            if (signupResponse != null) {
                model.addAttribute("user", new User());
                return "login";
            } else {
                return "error";
            }
        } else {
            return "userExists";
        }
    }
    @GetMapping("/users/{userId}")
    @PreAuthorize("@securityUtils.isUserIdMatch(#userId)")
    public String getUserById(@PathVariable Long userId, ModelMap model) {
         User user = userService.findById(userId);
         model.put("user", user);
         return "user";
    }

//    @GetMapping("/users/{userId}/artists")
//    public ResponseEntity<List<Artist>> getArtists(@PathVariable Long userId)
        
                                                   







    @GetMapping("/users/{userId}/top-artists")
    public String getUsersTopArtists(@PathVariable Long userId, Model model) {
        List<Artist> topArtists = userService.findTopArtists(userId);
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("topArtists", topArtists);
        return "top-artists";
    }
}
