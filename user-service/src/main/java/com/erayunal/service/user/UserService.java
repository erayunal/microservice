package com.erayunal.service.user;

import com.erayunal.dto.UserRegisterRequest;
import com.erayunal.dto.UserRegisterResponse;
import com.erayunal.user.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO UserDTO);
    UserDTO updateUser(Long id, UserDTO UserDTO);
    void deleteUser(Long id);
    UserRegisterResponse registerUser(UserRegisterRequest request);
}