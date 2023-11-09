package com.haydenhuffman.soundoffbandtracker.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Artist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;
    private String name;
    @OneToMany(mappedBy = "artist")
    private List<Performance> performances;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
