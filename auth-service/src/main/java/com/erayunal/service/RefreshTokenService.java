package com.erayunal.service;

import com.erayunal.dto.AuthResponse;
import com.erayunal.dto.TokenRefreshRequest;
import com.erayunal.entity.RefreshToken;
import com.erayunal.entity.User;
import com.erayunal.repository.RefreshTokenRepository;
import com.erayunal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-token.expiration-ms:604800000}") // 7 gün default
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public RefreshToken createRefreshToken(String username, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Aynı kullanıcı + cihazdan gelen token varsa önce onu sil
        deleteByUserAndClientInfo(user, userAgent, ipAddress);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public void deleteByToken(String refreshToken) {
        findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    public void deleteByUserAndClientInfo(User user, String userAgent, String ipAddress) {
        refreshTokenRepository
                .findByUserAndUserAgentAndIpAddress(user, userAgent, ipAddress)
                .ifPresent(refreshTokenRepository::delete);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public ResponseEntity<?> refreshToken(TokenRefreshRequest refreshRequest, HttpServletRequest request) {
        return findByToken(refreshRequest.getRefreshToken())
                .map(token -> {
                    if (isExpired(token)) {
                        deleteByToken(token.getToken());
                        return ResponseEntity.badRequest().body("Refresh token expired");
                    }

                    // Kullanıldıktan sonra refresh token silinsin
                    refreshTokenRepository.delete(token);

                    var userDetails = userDetailsService.loadUserByUsername(token.getUser().getUsername());
                    String newAccessToken = jwtService.generateToken(userDetails);

                    // Yeni refresh token oluşturulmalı
                    RefreshToken newRefreshToken = createRefreshToken(token.getUser().getUsername(), request);

                    return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken.getToken()));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Invalid refresh token"));
    }
}
