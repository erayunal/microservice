package com.erayunal.client.user;

import com.erayunal.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") String id);

    @PostMapping("/api/users/createUser")
    UserDTO createUser(@RequestBody UserDTO userDTO);

    @PostMapping("/api/users/findByUsername")
    UserDTO findByUsername(@RequestParam("username") String username);

}
