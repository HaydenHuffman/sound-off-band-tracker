package com.haydenhuffman.soundoffbandtracker.web;


import com.haydenhuffman.soundoffbandtracker.dao.request.RefreshTokenRequest;
import com.haydenhuffman.soundoffbandtracker.dao.request.SignInRequest;
import com.haydenhuffman.soundoffbandtracker.dao.response.JwtAuthenticationResponse;
import com.haydenhuffman.soundoffbandtracker.dao.response.TokenRefreshResponse;
import com.haydenhuffman.soundoffbandtracker.domain.RefreshToken;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.security.AuthenticationServiceImpl;
import com.haydenhuffman.soundoffbandtracker.security.JwtService;
import com.haydenhuffman.soundoffbandtracker.service.RefreshTokenService;
import com.haydenhuffman.soundoffbandtracker.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

//@RestController
//@RequestMapping("/api/v1/auth")
@Controller
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserServiceImpl userService;
    
    public AuthenticationController(AuthenticationServiceImpl authenticationService,
                                    RefreshTokenService refreshTokenService, JwtService jwtService, UserServiceImpl userService) {
		super();
		this.authenticationService = authenticationService;
		this.refreshTokenService = refreshTokenService;
		this.jwtService = jwtService;
		this.userService = userService;
	}
    

    @GetMapping("/signin")
	public String getLogin (@ModelAttribute("user") User user) {
		return "login";
	}
    

//    @GetMapping("/signin")
//    public String getLogin() {
//    	return "login";
//    }
    
    @GetMapping("/login-error")
    public String loginError (Model model) {
    	model.addAttribute("loginError", true);
    	return "login";
    }
    

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request, @RequestBody User user) {
    	Optional<User> existingUser = userService.findUserByEmail(user.getEmail());
//    	User loggedUser = ((User) userService).loadUserByUsername(user.getUsername());
    	String accessToken = jwtService.generateToken(user);
    	
        return ResponseEntity.ok(authenticationService.signin(request));
//    	User loggedUser = (User) userService.userDetailsService().loadUserByUsername(user.getUsername());
    	
    	// moving the below to the SecurityConfig SecFilterChain:
//    	String accessToken = jwtService.generateToken(loggedUser);
//    	RefreshToken refreshToken = refreshTokenService.createRefreshToken(loggedUser.getId());
    	
//        return ResponseEntity.ok(authenticationService.signin(request));
//    	return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, refreshToken.getToken()));
    }

    
//    @PostMapping("/signin")
//    public String authenticateLogin (@ModelAttribute("user") User user, SignInRequest request) {
//    	Optional<User> existingUser = userService.findUserByEmail(user.getEmail());
//    	User loggedUser = ((User) userService).loadUserByUsername(user.getUsername());
//    	String accessToken = jwtService.generateToken(user);
//    	
//        return ResponseEntity.ok(authenticationService.signin(request));
//    }
    
//    @PostMapping("/signin")
//    public ModelAndView authenticateLogin(@RequestBody User user, SignInRequest request) {
//    	Optional<User> loggedUser = userService.findUserByEmail(user.getEmail());
//    	
//    	String accessToken = jwtService.generateToken(user);
//    	
//    	RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
//    	
//    	return new ModelAndView(new RedirectView("/success"));
//    }
    
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshTokenRequest request) {
      String requestRefreshToken = request.refreshToken();

      return refreshTokenService.findByToken(requestRefreshToken)
          .map(refreshTokenService::verifyExpiration)
          .map(RefreshToken::getUser)
          .map(user -> {
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
          })
          .orElseThrow(() -> new IllegalStateException(
              "Refresh token " + requestRefreshToken + " is not in database!"));
    }
}