package com.haydenhuffman.soundoffbandtracker.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String username;
    private String password;
    private String email;
    @OneToMany(mappedBy = "user")
    private List<Artist> artists;
}
