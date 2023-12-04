package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

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
        performance.setPerfScore(performance.getAttendance() * getDayOfWeekScore(performance.getDate()));
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
    public double getDayOfWeekScore(LocalDate date) {
        Map<DayOfWeek, Double> daysOfWeekScore = Map.of(
                DayOfWeek.MONDAY, 1.0,
                DayOfWeek.TUESDAY, 1.3,
                DayOfWeek.WEDNESDAY, 1.5,
                DayOfWeek.THURSDAY, 1.4,
                DayOfWeek.FRIDAY, 1.25,
                DayOfWeek.SATURDAY, 0.67,
                DayOfWeek.SUNDAY, 0.67
        );

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return daysOfWeekScore.getOrDefault(dayOfWeek, 1.0);
    }

}

