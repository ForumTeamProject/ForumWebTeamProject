package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
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
    public void create(Role role, User authenticatedUser) {
        if (!(authenticatedUser.getRoles().contains(roleRepository.getByName("admin")))) {
            throw new UnauthorizedOperationException("You do not have permission to change roles");
        }
        checkRoleUniqueness(role);
        roleRepository.create(role);
    }

    @Override
    public void update(Role role, User authenticatedUser) {
        if (!(authenticatedUser.getRoles().contains(roleRepository.getByName("admin")))) {
            throw new UnauthorizedOperationException("You do not have permission to change roles");
        }
        checkRoleUniqueness(role);
        roleRepository.update(role);
    }

    @Override
    public void delete(int id, User authenticatedUser) {
        if (!(authenticatedUser.getRoles().contains(roleRepository.getByName("admin")))) {
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
        if (duplicateExists) {
            throw new DuplicateEntityException("Role", role.getId());
        }
    }
}