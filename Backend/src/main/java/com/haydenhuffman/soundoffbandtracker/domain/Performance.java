package com.haydenhuffman.soundoffbandtracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

@Entity
public class Performance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;
    private LocalDate date;
    private Integer attendance;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;
    private Double perfScore;


    
    public Performance() {
    }

    public Performance(LocalDate date, Integer attendance) {
        this.date = date;
        this.attendance = attendance;
        this.perfScore = getDayOfWeekScore(date);
    }
    

    public Long getPerformanceId() {
        return performanceId;
    }
    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Integer getAttendance() {
        return attendance;
    }
    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }
    public Artist getArtist() {
        return artist;
    }
    public void setArtist(Artist artist) {
        this.artist = artist;
    }
    public Double getPerfScore() {
        return perfScore;
    }
    public void setPerfScore(Double perfScore) {
        this.perfScore = perfScore;
    }


    @Override
    public String toString() {
        return "Performance [performanceId=" + performanceId + ", date=" + date + ", attendance=" + attendance
                + ", artist=" + artist + ", perfScore=" + perfScore + "]";
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
