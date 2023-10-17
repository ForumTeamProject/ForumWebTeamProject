package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface UserRepository {
    //potential filtering
    User get(int id);

    List<User> getAll();

    User get(String username);

    User findByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int id);
}
