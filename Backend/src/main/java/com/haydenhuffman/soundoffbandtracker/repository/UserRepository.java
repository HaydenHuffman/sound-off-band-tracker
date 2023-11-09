package com.haydenhuffman.soundoffbandtracker.repository;

import com.haydenhuffman.soundoffbandtracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
