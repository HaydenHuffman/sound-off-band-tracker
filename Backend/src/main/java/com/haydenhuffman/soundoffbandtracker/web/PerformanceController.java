package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.PerformanceRepository;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import com.haydenhuffman.soundoffbandtracker.service.PerformanceService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users/{userId}/{artistId}")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final PerformanceRepository performanceRepository;
    private final ArtistService artistService;
    private final UserServiceImpl userService;

    public PerformanceController(UserServiceImpl userService, PerformanceService performanceService, ArtistService artistService, PerformanceRepository performanceRepository) {
        this.performanceService = performanceService;
        this.artistService = artistService;
        this.performanceRepository = performanceRepository;
        this.userService = userService;
    }

    @PostMapping("/add")
    public String addPerformance(@PathVariable Long userId, @PathVariable Long artistId, @ModelAttribute Performance performance) {
        performanceService.createPerformance(performance, artistId);
        // System.out.println(performance);
        return "redirect:/users/" + userId + "/" + artistId;
    }

    @GetMapping("/{performanceId}")
    public String getPerformance(@PathVariable Long userId, @PathVariable Long artistId, @PathVariable Long performanceId, ModelMap model) {
        Artist artist = artistService.findById(artistId);
        User user = userService.findById(userId);
        Performance performance = performanceService.findById(performanceId);
        model.put("artist", artist);
        model.put("user", user);
        model.put("performance", performance);
        return "performance";
    }

    @PostMapping("/{performanceId}/delete")
    public String deletePerformance(@PathVariable Long performanceId, @PathVariable Long userId, @PathVariable Long artistId) {
        performanceService.deleteById(performanceId);
        return "redirect:/users/" + userId + "/" + artistId;
    }

    @PostMapping("/{performanceId}/edit")
    public String postEditPerformance(@ModelAttribute Performance performance, @PathVariable Long userId, @PathVariable Long artistId) {
        Artist artist = artistService.findById(artistId);
        performance.setArtist(artist);
        performanceService.save(performance);
        return "redirect:/users/" + userId + "/" + artistId;
    }
}
