package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Authority;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    public UserServiceImpl(UserRepository userRepository, ArtistService artistService, PerformanceService performanceService) {
        this.userRepository = userRepository;
        this.artistService = artistService;
        this.performanceService = performanceService;
    }

    public User findById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElse(new User());
    }

    public void addUserData(User user) {
        LocalDate date = LocalDate.now();
        Random random = new Random();
        List<Artist> newArtists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Artist newArtist = artistService.createRandomArtist(user, new Artist());
            List<Performance> performances = new ArrayList<>();
            for (int j = 3; j < 6; j++) {
                Double attendance = (double) (random.nextInt(81) + 20); // Create random attendance between 80 and 100
                Double sales = (double) (random.nextInt(1501) + 1500); // Create random $ between 1500 - 3000
                date = date.minusDays(j);
                Performance newPerformance = performanceService.createPerformance(new Performance(date, attendance, sales), newArtist.getArtistId());
                performanceService.save(newPerformance);
            }
            newArtist.setUser(user);
            user.addArtist(newArtist);
            artistService.save(user, newArtist);
        }
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

    @Secured({"ROLE_ADMIN"})
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Secured("ROLE_ADMIN")
    @Transactional // This annotation ensures that changes are committed to the database
    public void elevateUserToAdmin(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the user doesn't already have the admin role
            if (user.getAuthorities().stream().noneMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()))) {
                // Add the admin role to the user
                Authority adminAuthority = new Authority("ROLE_ADMIN");
                adminAuthority.setUser(user);
                user.getAuthorities().add(adminAuthority);

//                logger.info("Setting Auth for user: " + user.getId() + user.getEmail());
//                logger.info("Setting Authorities: " + user.getAuthorities());

                // Save the updated user
                userRepository.save(user);
            }
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
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
