package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll(){
        return roleRepository.getAll();
    }
    @Override
    public Role getById(int id) {
        return this.roleRepository.getById(id);
    }

    @Override
    public Role getByName(String name) {
        return this.roleRepository.getByName(name);
    }

    @Override
    public void create(Role role) {
        checkRoleUniqueness(role);
        roleRepository.create(role);
    }

    @Override
    public void update(Role role, RegisteredUser authenticatedUser) {
        if (!(authenticatedUser.getRole().equals("admin"))) {
            throw new UnauthorizedOperationException("You do not have permission to change roles");
        }
        roleRepository.update(role);
    }

    @Override
    public void delete(int id, RegisteredUser authenticatedUser) {
        if (!(authenticatedUser.getRole().equals("admin"))) {
            throw new UnauthorizedOperationException("You do not have permission to change roles");
        }
        roleRepository.delete(id);
    }

    private void checkRoleUniqueness(Role role) {
        boolean duplicateExists = true;
        try {
            roleRepository.getByName(role.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
    }
}