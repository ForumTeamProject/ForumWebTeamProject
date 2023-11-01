package com.example.forumsystemwebproject.helpers.mappers;


import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.models.DTOs.UserDto;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    private final UserService service;

    private final RoleRepository roleRepository;

    @Autowired
    public UserMapper(UserService service, RoleRepository roleRepository) {
        this.service = service;
        this.roleRepository = roleRepository;
    }

    public User fromDto(int id, UserDto dto) {
        User user = fromDto(dto);
        user.setId(id);
        User repositoryUser = service.getById(id);
        user.setRoles(repositoryUser.getRoles());
        return user;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getByName("user"));
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRoles(roles);
        return user;
    }

}
