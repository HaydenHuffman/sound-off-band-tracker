package com.haydenhuffman.soundoffbandtracker.domain;

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
    private Artist artist;
}
