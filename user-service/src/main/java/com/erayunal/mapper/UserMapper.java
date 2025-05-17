package com.erayunal.mapper;

import com.erayunal.entity.User;
import com.erayunal.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles()
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRoles(dto.getRoles()
                .stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet()));
        return user;
    }
}