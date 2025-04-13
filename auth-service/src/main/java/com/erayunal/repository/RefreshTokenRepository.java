package com.erayunal.repository;

import com.erayunal.entity.RefreshToken;
import com.erayunal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserAndUserAgentAndIpAddress(User user, String userAgent, String ipAddress);

    int deleteByUser(User user);

}