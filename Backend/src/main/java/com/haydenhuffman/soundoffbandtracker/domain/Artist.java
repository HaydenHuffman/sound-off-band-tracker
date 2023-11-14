package com.haydenhuffman.soundoffbandtracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;
    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Performance> performances;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String image;
    private Integer aggScore;

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", image='" + image + '\'' +
                '}';
    }
}
