package com.haydenhuffman.soundoffbandtracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;
    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "artist", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Performance> performances;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;
    private String image;
    private Double aggScore;

    public Artist() {
    }

    public Artist(User user, List<Performance> performances) {
        this.user = user;
        this.performances = performances;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Performance> getPerformances() {
        if (this.performances == null) {
            this.performances = new ArrayList<>();
        }
        return  this.performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getAggScore() {
        return aggScore;
    }

    public void setAggScore(Double aggScore) {
        this.aggScore = aggScore;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
//                ", performances=" + performances +
                ", user=" + user +
                ", image='" + image + '\'' +
//                ", aggScore=" + aggScore +
                '}';
    }
}
