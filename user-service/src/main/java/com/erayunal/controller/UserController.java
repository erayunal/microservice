package com.erayunal.controller;

import com.erayunal.dto.UserRegisterRequest;
import com.erayunal.dto.UserRegisterResponse;
import com.erayunal.service.user.UserService;
import com.erayunal.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO UserDTO) {
        return userService.createUser(UserDTO);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO UserDTO) {
        return userService.updateUser(id, UserDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }
}
