package com.haydenhuffman.soundoffbandtracker.dto;

import java.time.LocalDate;

public class PerformanceDTO {

    private Long performanceId;
    private LocalDate date;
    private Double attendance;
    private Double perfScore;
    private Double sales;
    private Long artistId;

    public PerformanceDTO() {
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

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
}
