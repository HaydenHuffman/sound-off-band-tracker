package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/{userId}")
    public String findById(@PathVariable Long userId, ModelMap model) {
         User user = userService.findById(userId);
         model.put("user", user);
         return "user";
    }

    @GetMapping("/home/{userId}")
    public String getHomePage(ModelMap model, @PathVariable Long userId) {
//        model.put("artist", artist);
        return null;
    }
}
