package com.example.forumsystemwebproject.helpers.mappers;

import com.example.forumsystemwebproject.models.DTOs.RoleDto;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    private final RoleService roleService;

    @Autowired
    public RoleMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public Role fromDto(int id, RoleDto dto) {
        Role role = fromDto(dto);
        role.setName(dto.getName());
        role.setId(id);
        return role;
    }

    public Role fromDto(RoleDto dto) {
        Role role = new Role();
        role.setName(dto.getName());
        return role;
    }
}
