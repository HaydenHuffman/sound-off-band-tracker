package com.haydenhuffman.soundoffbandtracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Performance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;
    private LocalDate date;
    private Double attendance;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;
    private Double perfScore;
    private Double sales;
    private Double perPersonAverage;

    public Performance() {
    }

    public Performance (LocalDate date, Double attendance) {
        this.date = date;
        this.attendance = attendance;
        this.artist = artist;
    }

    public Performance(LocalDate date, Double attendance, Double sales) {
        this.date = date;
        this.attendance = attendance;
        this.sales = sales;
        this.artist = artist;
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
    public Double getAttendance() {
        return attendance;
    }
    public void setAttendance(Double attendance) {
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
    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }
    public Double getPerPersonAverage() {
        return perPersonAverage;
    }
    public void setPerPersonAverage(Double perPersonAverage) {
        this.perPersonAverage = perPersonAverage;
    }



    @Override
    public String toString() {
        return "Performance [performanceId=" + performanceId + ", date=" + date + ", attendance=" + attendance
                + ", artist=" + artist + ", perfScore=" + perfScore + "]";
    }


}
