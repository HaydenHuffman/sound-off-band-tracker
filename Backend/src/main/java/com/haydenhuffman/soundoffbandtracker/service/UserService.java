package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User findById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElse(new User());
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
