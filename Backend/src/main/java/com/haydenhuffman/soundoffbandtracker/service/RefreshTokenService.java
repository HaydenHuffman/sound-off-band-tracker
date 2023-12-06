package com.haydenhuffman.soundoffbandtracker.service;


import com.haydenhuffman.soundoffbandtracker.dao.request.RefreshTokenRequest;
import com.haydenhuffman.soundoffbandtracker.domain.RefreshToken;
import com.haydenhuffman.soundoffbandtracker.repository.RefreshTokenRepository;
import com.haydenhuffman.soundoffbandtracker.repository.UserRepository;
import com.haydenhuffman.soundoffbandtracker.security.JwtServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
	@Value("${token.refreshExpiration}")
	private Long refreshTokenDurationMs;

	private RefreshTokenRepository refreshTokenRepository;
	private UserRepository userRepository;
	private JwtServiceImpl jwtService;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository,
                               JwtServiceImpl jwtService) {
		super();
		this.refreshTokenRepository = refreshTokenRepository;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);

			throw new IllegalStateException(
					"Refresh token " + token.getToken() + " was expired. Please make a new signin request");
		}

		return token;
	}

	@Transactional
	public int deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}

	public String createNewAccessToken(RefreshTokenRequest refreshTokenRequest) {
		Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(refreshTokenRequest.refreshToken());
		String accessToken = refreshTokenOpt
				.map(this::verifyExpiration)
				.map(refreshToken -> jwtService.generateToken(new HashMap<>(), refreshToken.getUser()))
				.orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
		return accessToken;
	}
}