package com.erayunal.service;

import com.erayunal.client.user.UserClient;
import com.erayunal.dto.LoginRequest;
import com.erayunal.dto.TokenRefreshRequest;
import com.erayunal.dto.auth.AuthResponse;
import com.erayunal.dto.user.UserDTO;
import com.erayunal.entity.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

//    private final UserRepository userRepository;
    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(UserDTO userDTO, HttpServletRequest request) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO = userClient.createUser(userDTO);

        var jwtToken = jwtService.generateToken(userDTO);
        var refreshToken = refreshTokenService.createRefreshToken(userDTO.getUsername(), request);
        return new AuthResponse(jwtToken, refreshToken.getToken());
    }

    public AuthResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        UserDTO user = userClient.findByUsername(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), request);
        return new AuthResponse(jwtToken, refreshToken.getToken());
    }

    public void logout(TokenRefreshRequest request) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
    }

    public void logoutAll(TokenRefreshRequest request) {
        refreshTokenService.findByToken(request.getRefreshToken())
                .map(RefreshToken::getUsername)
                .ifPresent(refreshTokenService::deleteByUsername);
    }
}
