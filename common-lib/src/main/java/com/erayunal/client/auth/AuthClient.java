package com.erayunal.client.auth;

import com.erayunal.dto.auth.AuthResponse;
import com.erayunal.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @PostMapping("/api/auth/register")
    AuthResponse registerUser(@RequestBody UserDTO userDTO);
}
