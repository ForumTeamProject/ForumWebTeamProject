package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;

import java.util.List;

public interface RoleService {
    List<Role> getAll();
    Role getById(int id);

    Role getByName(String name);

    void create(Role role);

    public void update(Role role, RegisteredUser authenticatedUser);

    public void delete(int id, RegisteredUser authenticatedUser);
}
