package com.erayunal.service;

import com.erayunal.dto.AuthResponse;
import com.erayunal.dto.LoginRequest;
import com.erayunal.dto.RegisterRequest;
import com.erayunal.dto.TokenRefreshRequest;
import com.erayunal.entity.RefreshToken;
import com.erayunal.entity.User;
import com.erayunal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(RegisterRequest registerRequest, HttpServletRequest request) {
        var user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);

        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), request);
        return new AuthResponse(jwtToken, refreshToken.getToken());
    }

    public AuthResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), request);
        return new AuthResponse(jwtToken, refreshToken.getToken());
    }

    public void logout(TokenRefreshRequest request) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
    }

    public void logoutAll(TokenRefreshRequest request) {
        refreshTokenService.findByToken(request.getRefreshToken())
                .map(RefreshToken::getUser)
                .ifPresent(refreshTokenService::deleteByUser);
    }
}
