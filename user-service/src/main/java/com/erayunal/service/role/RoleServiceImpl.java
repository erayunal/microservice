package com.erayunal.service.role;

import com.erayunal.entity.Role;
import com.erayunal.mapper.RoleMapper;
import com.erayunal.repository.RoleRepository;
import com.erayunal.role.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        return roleMapper.toDto(roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found")));
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDto) {
        Role role = roleMapper.toEntity(roleDto);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(roleDto.getName());
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
