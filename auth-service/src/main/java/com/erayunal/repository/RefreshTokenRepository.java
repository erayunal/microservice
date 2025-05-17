package com.erayunal.repository;

import com.erayunal.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUsernameAndUserAgentAndIpAddress(String username, String userAgent, String ipAddress);

    int deleteByUsername(String username);

}