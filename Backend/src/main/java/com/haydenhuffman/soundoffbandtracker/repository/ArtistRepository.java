package com.haydenhuffman.soundoffbandtracker.repository;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
