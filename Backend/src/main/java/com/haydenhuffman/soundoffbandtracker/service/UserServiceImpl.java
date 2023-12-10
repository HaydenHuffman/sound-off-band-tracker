package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ArtistService artistService;

    public UserServiceImpl(UserRepository userRepository, @Lazy ArtistService artistService) {
        this.userRepository = userRepository;
        this.artistService = artistService;
    }

    public User findById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElse(new User());
    }

    public User createUser(User user) {
        LocalDate date = LocalDate.now();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Artist newArtist = artistService.createNewArtist(user, new Artist());
            List<Performance> performances = new ArrayList<>();
            for (int j = 0; j < 2; j++){
                Double attendance = random.nextDouble(81) + 20;
                performances.add(new Performance(date, attendance));
                date = date.minusDays(j);
            }
            newArtist.setPerformances(performances);
        }
        return userRepository.save(user);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

                List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                        .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                        .collect(Collectors.toList());

                return user;
            }
        };
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) {
//    	User user = userRepository.findByEmail(username)
//    			.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
//
//    	List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
//    			.map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
//    			.collect(Collectors.toList());
//
//    	return user;
//    }

    //    @Override
    @Secured({"ROLE_ADMIN"})
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//			throw new UserAlreadyExistsException("A user with this email already exists");
            return null;
        }
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

}
