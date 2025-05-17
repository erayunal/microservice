package com.erayunal.service.user;

import com.erayunal.dto.UserRegisterRequest;
import com.erayunal.dto.UserRegisterResponse;
import com.erayunal.entity.User;
import com.erayunal.mapper.UserMapper;
import com.erayunal.repository.UserRepository;
import com.erayunal.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public UserDTO createUser(UserDTO UserDTO) {
        User user = userMapper.toEntity(UserDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO UserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(UserDTO.getUsername());
        user.setEmail(UserDTO.getEmail());

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        UserRegisterResponse response = new UserRegisterResponse();
        response.setId(saved.getId());
        response.setUsername(saved.getUsername());
        response.setEmail(saved.getEmail());

        return response;
    }
}