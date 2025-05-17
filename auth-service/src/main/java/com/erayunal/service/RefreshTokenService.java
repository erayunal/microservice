package com.erayunal.service;

import com.erayunal.client.user.UserClient;
import com.erayunal.dto.TokenRefreshRequest;
import com.erayunal.dto.auth.AuthResponse;
import com.erayunal.dto.user.UserDTO;
import com.erayunal.entity.RefreshToken;
import com.erayunal.repository.RefreshTokenRepository;
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

    private final UserClient userClient;
    @Value("${jwt.refresh-token.expiration-ms:604800000}") // 7 gün default
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshToken createRefreshToken(String username, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        UserDTO user = userClient.findByUsername(username);

        // Aynı kullanıcı + cihazdan gelen token varsa önce onu sil
        deleteByUserAndClientInfo(username, userAgent, ipAddress);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(user.getUsername())
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
    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }

    public void deleteByToken(String refreshToken) {
        findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    public void deleteByUserAndClientInfo(String username, String userAgent, String ipAddress) {
        refreshTokenRepository
                .findByUsernameAndUserAgentAndIpAddress(username, userAgent, ipAddress)
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

                    UserDTO userDTO = userClient.findByUsername(token.getUsername());
                    String newAccessToken = jwtService.generateToken(userDTO);

                    // Yeni refresh token oluşturulmalı
                    RefreshToken newRefreshToken = createRefreshToken(token.getUsername(), request);

                    return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken.getToken()));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Invalid refresh token"));
    }
}
