package com.haydenhuffman.soundoffbandtracker.service;

import com.coderscampus.SpringSecurityJWTDemo.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    List<User> findAll();
}