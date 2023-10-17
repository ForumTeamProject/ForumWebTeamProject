package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.models.UserModels.User;

import java.util.List;

public interface UserService {
    User get(int id);

    List<User> getAll();

    User get(String username);

    User findByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int id);
}
