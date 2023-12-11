
package com.haydenhuffman.soundoffbandtracker.security;


import com.haydenhuffman.soundoffbandtracker.dao.request.SignInRequest;
import com.haydenhuffman.soundoffbandtracker.dao.request.SignUpRequest;
import com.haydenhuffman.soundoffbandtracker.dao.response.JwtAuthenticationResponse;
import com.haydenhuffman.soundoffbandtracker.domain.Authority;
import com.haydenhuffman.soundoffbandtracker.domain.Role;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.UserRepository;
import com.haydenhuffman.soundoffbandtracker.service.RefreshTokenService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceImpl userService;
    
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                     JwtService jwtService, AuthenticationManager authenticationManager,
                                     UserServiceImpl userService, RefreshTokenService refreshTokenService) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }



	@Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = new User()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .authority(Role.USER.name()).build();
        request.authorityOpt().ifPresent(auth -> user.getAuthorities().add(new Authority(auth, user)));
        userRepository.save(user);
        userService.addUserData(user);

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUserId());
        
        String encodedPassword = passwordEncoder.encode(request.password());
        logger.error("Raw Password: {}, Encoded Password: {}", request.password(), encodedPassword);
        
        return new JwtAuthenticationResponse(jwt, refreshToken.getToken());
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        var refreshTokenOpt = refreshTokenService.findByToken(jwt);
        
        logger.error("Provided password during login: {}", request.password());
        logger.error("Encoded password during login: {}", user.getPassword()); // Print the encoded password from the database
        
        if (refreshTokenOpt.isPresent()) {
            return new JwtAuthenticationResponse(jwt, refreshTokenOpt.get().getToken()); 
        } else {
            return new JwtAuthenticationResponse(jwt, refreshTokenService.createRefreshToken(user.getUserId()).getToken());
        }
    }
}

