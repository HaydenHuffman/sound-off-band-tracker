package com.haydenhuffman.soundoffbandtracker.web;


import com.haydenhuffman.soundoffbandtracker.dao.request.SignUpRequest;
import com.haydenhuffman.soundoffbandtracker.dao.response.JwtAuthenticationResponse;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.security.AuthenticationServiceImpl;
import com.haydenhuffman.soundoffbandtracker.security.JwtServiceImpl;
import com.haydenhuffman.soundoffbandtracker.service.RefreshTokenService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class RegistrationController {
	
	private Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	
//	private PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	private UserServiceImpl userService;
	private AuthenticationServiceImpl authenticationService;
	private JwtServiceImpl jwtService;
	private RefreshTokenService refreshTokenService;
	private PasswordEncoder passwordEncoder;

	
	public RegistrationController(UserServiceImpl userService, AuthenticationServiceImpl authenticationService,
			JwtServiceImpl jwtService, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder) {
		super();
		this.userService = userService;
		this.authenticationService = authenticationService;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.passwordEncoder = passwordEncoder;
	}


	@GetMapping("/register")
	public String getRegistration (ModelMap model) {
		model.addAttribute("user", new User());
		return "index";
	}
	
	
	@PostMapping("/register")
	public String processRegistration(@ModelAttribute("user") User user, SignUpRequest request) {
	    Optional<User> existingUser = userService.findUserByEmail(user.getEmail());
	    String encodedPassword = passwordEncoder.encode(request.password());
	    

	    if (existingUser.isPresent()) {
	    	logger.info("User already exists. Redirecting to userExists.");
	        // Redirect to the userExists page if a user with the same email exists
	        return "userExists";
	    } else {
	    	JwtAuthenticationResponse signupResponse = authenticationService.signup(request);
	    	logger.info("This data is from the ProcessRegistration in the RegistrationController");
	    	logger.info("Processing registration for user: " + user.getEmail());
	    	logger.error("Provided password during registration: " + request.password());
	    	logger.info("Encoded password during registration: " + encodedPassword);
			
	        if (signupResponse != null) {
	            // Successfully registered user, now proceed with authentication
	                logger.info("Successfully registered user. Redirecting to success.");
	                return "login";
	            } else {
	                // Handle the case where authentication is not successful
	            	logger.info("User registration failed. Redirecting to error.");
	                return "error";
	            }
	        }
	    }
	}

//  @PostMapping("/signup")
//  public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
//      return ResponseEntity.ok(authenticationService.signup(request));
//  }

