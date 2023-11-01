package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Role getById(int id);

    Role getByName(String name);

    void create(Role role, User authenticatedUser);

    public void update(Role role, User authenticatedUser);

    public void delete(int id, User authenticatedUser);
}
