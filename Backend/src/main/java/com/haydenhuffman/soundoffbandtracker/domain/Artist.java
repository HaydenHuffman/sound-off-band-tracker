package com.haydenhuffman.soundoffbandtracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
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
    private Double aggScore;

    public Artist() {
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
        return performances;
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
        if (performances == null || performances.isEmpty()) {
            return 0.0;
        }
        return calculateAverageRating() * (1 + (1 / daysSinceLastPerformance()));
    }

    public void setAggScore(Double aggScore) {
        this.aggScore = aggScore;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                ", performances=" + performances +
                ", user=" + user +
                ", image='" + image + '\'' +
                ", aggScore=" + aggScore +
                '}';
    }

    public Double calculateAverageRating() {
        if (performances == null || performances.isEmpty()) {
        return 0.0;
        }
        double totalRating = performances.stream()
                .map(Performance::getPerfScore)
                .filter(Objects::nonNull)
                .mapToDouble(perfScore -> perfScore)
                .average()
                .orElse(0.0);

        return totalRating;
    }

    public Long daysSinceLastPerformance() {
        if (performances == null || performances.isEmpty()) {
            return 0L;
        }

        return performances.stream()
                        .filter(performance -> performance.getDate() != null)
                        .max(Comparator.comparing(Performance::getDate))
                        .map(Performance::getDate)
                        .map(date -> ChronoUnit.DAYS.between(date, LocalDate.now()))
                        .orElse(0L);
    }
}
