package com.haydenhuffman.soundoffbandtracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Performance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;
    private LocalDate date;
    private Integer attendance;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;
    private Integer perfScore;

    @Override
    public String toString() {
        return "Performance{" +
                "performanceId=" + performanceId +
                ", date=" + date +
                ", attendance=" + attendance +
                '}';
    }
}
