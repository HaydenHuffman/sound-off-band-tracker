package com.haydenhuffman.soundoffbandtracker.security;


import com.haydenhuffman.soundoffbandtracker.dao.request.SignInRequest;
import com.haydenhuffman.soundoffbandtracker.dao.request.SignUpRequest;
import com.haydenhuffman.soundoffbandtracker.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
}