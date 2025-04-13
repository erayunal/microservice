package com.erayunal.controller;

import com.erayunal.dto.AuthResponse;
import com.erayunal.dto.LoginRequest;
import com.erayunal.dto.RegisterRequest;
import com.erayunal.dto.TokenRefreshRequest;
import com.erayunal.service.AuthService;
import com.erayunal.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        return ResponseEntity.ok(authService.register(registerRequest, request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(loginRequest, request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest refreshRequest, HttpServletRequest request) {
        return refreshTokenService.refreshToken(refreshRequest, request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRefreshRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenRefreshRequest request) {
        authService.logoutAll(request);
        return ResponseEntity.ok("Logged out from all devices");
    }
}
