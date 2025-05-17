package com.erayunal.mapper;

import com.erayunal.entity.Role;
import com.erayunal.dto.role.RoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDTO toDto(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    public Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }
}