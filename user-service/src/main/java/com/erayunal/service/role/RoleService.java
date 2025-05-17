package com.erayunal.service.role;

import com.erayunal.role.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Long id);
    RoleDTO createRole(RoleDTO RoleDTO);
    RoleDTO updateRole(Long id, RoleDTO RoleDTO);
    void deleteRole(Long id);
}