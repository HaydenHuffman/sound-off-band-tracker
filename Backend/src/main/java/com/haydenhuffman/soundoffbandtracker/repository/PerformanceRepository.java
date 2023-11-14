package com.haydenhuffman.soundoffbandtracker.repository;

import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
}
