package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.dto.PerformanceDTO;
import com.haydenhuffman.soundoffbandtracker.repository.PerformanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private PerformanceRepository performanceRepository;
    private ArtistService artistService;

    public PerformanceService(PerformanceRepository performanceRepository, ArtistService artistService) {
        this.performanceRepository = performanceRepository;
        this.artistService = artistService;
    }

    @Transactional
    public void save(Performance performance) {
        findAndSetPerPersonAverage(performance);
        performance.setPerfScore(calculatePerformanceScore(performance));
        performanceRepository.save(performance);
        performance.getArtist().setAggScore(artistService.calculateAggScore(performance.getArtist()));
    }

    private static void findAndSetPerPersonAverage(Performance performance) {
        BigDecimal sales = new BigDecimal(performance.getSales());
        BigDecimal attendance = new BigDecimal(performance.getAttendance());
        BigDecimal average = sales.divide(attendance, 2, RoundingMode.HALF_UP);
        performance.setPerPersonAverage(average.doubleValue());
    }

    public Performance createPerformance(Performance performance, Long artistId) {
        Artist currentArtist = artistService.findById(artistId);
        findAndSetPerPersonAverage(performance);
        double score = calculatePerformanceScore(performance);
        performance.setPerfScore(score);
        performance.setArtist(currentArtist);
        currentArtist.getPerformances().add(performance);
        artistService.updateArtistAggScore(currentArtist);
        return performance;
    }

    private double calculatePerformanceScore(Performance performance) {
        double score = performance.getAttendance() * getDayOfWeekScore(performance.getDate()) * (performance.getPerPersonAverage() / 25); // This number is hardcoded based on my venue. Either add your own or create logic to compare it to the average PPA for all performances
        score = Math.round(score * 10.0) / 10.0; // Rounding to 1 decimal place
        return score;
    }


    public Performance findById(Long performanceId) {
        return performanceRepository.findById(performanceId).orElse(new Performance());
    }

    public void deleteById(Long artistId, Long performanceId) {
        Artist currentArtist = artistService.findById(artistId);
        Performance currentPerformance = performanceRepository.findById(performanceId).orElse(new Performance());
        artistService.findById(artistId).getPerformances().remove(currentPerformance);
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

    public List<Performance> findPerformancesByArtistId(Long artistId) {
        return performanceRepository.findAll()
                .stream()
                .filter(performance -> performance.getArtist() != null && artistId.equals(performance.getArtist().getArtistId()))
                .collect(Collectors.toList());

    }

    public Performance findPerformanceById(Long performanceId) {
        return (performanceRepository.findById(performanceId)).orElse(new Performance());
    }

    public PerformanceDTO convertToDTO(Performance performance) {
        PerformanceDTO dto = new PerformanceDTO();
        dto.setPerformanceId(performance.getPerformanceId());
        dto.setDate(performance.getDate());
        dto.setAttendance(performance.getAttendance());
        dto.setPerfScore(performance.getPerfScore());
        dto.setSales(performance.getSales());

        if (performance.getArtist() != null) {
            dto.setArtistId(performance.getArtist().getArtistId());
        }

        return dto;
    }
}

