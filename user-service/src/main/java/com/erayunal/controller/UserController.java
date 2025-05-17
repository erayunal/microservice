package com.erayunal.controller;

import com.erayunal.dto.user.UserDTO;
import com.erayunal.service.user.UserService;
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
    public UserDTO getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/findByUsername")
    public UserDTO findByUsername(@RequestParam("username") String username) throws Exception {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/save")
    public UserDTO saveUser(@RequestBody UserDTO UserDTO) {
        return userService.updateUser(UserDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }
}
