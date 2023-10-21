package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.models.Role;

import java.util.List;


public interface RoleRepository {
    Role getById(int id);

    Role getByName(String name);

    void create(Role role);

    void update(Role role);

    void delete(int id);
    List<Role> getAll();
}
