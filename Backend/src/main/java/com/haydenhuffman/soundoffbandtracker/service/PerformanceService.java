package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    private PerformanceRepository performanceRepository;
    private ArtistService artistService;

    public PerformanceService(PerformanceRepository performanceRepository, ArtistService artistService) {
        this.performanceRepository = performanceRepository;
        this.artistService = artistService;
    }
    public Performance save(Performance performance) {
        performanceRepository.save(performance);
        return performance;
    }

    public void createPerformance(Performance performance, Long artistId) {
        Artist currentArtist = artistService.findById(artistId);
        performance.setArtist(currentArtist);
        currentArtist.getPerformances().add(performance);
        performanceRepository.save(performance);

    }

    public Performance findById(Long performanceId) {
        return performanceRepository.findById(performanceId).orElse(new Performance());
    }

    public void deleteById(Long performanceId) {
        performanceRepository.deleteById(performanceId);
    }
}
