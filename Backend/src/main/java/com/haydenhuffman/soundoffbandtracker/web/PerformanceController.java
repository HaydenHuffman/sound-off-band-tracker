package com.haydenhuffman.soundoffbandtracker.web;

import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.service.ArtistService;
import com.haydenhuffman.soundoffbandtracker.service.PerformanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users/{userId}/{artistId}")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final ArtistService artistService;

    public PerformanceController(PerformanceService performanceService, ArtistService artistService) {
        this.performanceService = performanceService;
        this.artistService = artistService;
    }

    @PostMapping("/add")
    public String addPerformance(@PathVariable Long userId, @PathVariable Long artistId, @ModelAttribute Performance performance) {
        performanceService.createPerformance(performance, artistId);
        // System.out.println(performance);
        return "redirect:/users/" + userId + "/" + artistId + "/" + performance.getPerformanceId();
    }

    @GetMapping("/{performanceId}")
    public String getPerformance(@PathVariable Long artistId, @PathVariable Long performanceId, ModelMap model) {
        Performance performance = performanceService.findById(performanceId);
        model.put("performance", performance);
        return "performance";
    }

    @PostMapping("/{performanceId}/delete")
    public String deletePerformance(@PathVariable Long performanceId, @PathVariable Long userId, @PathVariable Long artistId) {
        performanceService.deleteById(performanceId);
        return "redirect:/users/" + userId + "/" + artistId;
    }
}
