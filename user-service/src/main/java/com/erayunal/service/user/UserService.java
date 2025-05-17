package com.erayunal.service.user;

import com.erayunal.dto.user.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO registerUser(UserDTO request);
}