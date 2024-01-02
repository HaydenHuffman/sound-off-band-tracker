package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.UserRepository;
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
    private PerformanceService performanceService;

    public UserServiceImpl(UserRepository userRepository, ArtistService artistService, PerformanceService performanceService) {
        this.userRepository = userRepository;
        this.artistService = artistService;
        this.performanceService = performanceService;
    }

    public User findById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElse(new User());
    }

    public User addUserData(User user) {
        LocalDate date = LocalDate.now();
        Random random = new Random();
        List<Artist> newArtists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Artist newArtist = artistService.createRandomArtist(user, new Artist());
            List<Performance> performances = new ArrayList<>();
            for (int j = 3; j < 6; j++) {
                Double attendance = (double) (random.nextInt(81) + 20);
                date = date.minusDays(j);
                performanceService.createPerformance(new Performance(date, attendance), newArtist.getArtistId());
            }
            newArtist.setUser(user);
            user.addArtist(newArtist);
            artistService.save(newArtist);
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

    public List<Artist> findTopArtists(Long userId) {
        User user = findById(userId);
        List<Artist> topArtists = user.getArtists().stream()
                .filter(artist -> artist.getAggScore() != null)
                .sorted((a1, a2) -> a2.getAggScore().compareTo(a1.getAggScore()))
                .limit(5)
                .toList();
        return topArtists;
    }

}
