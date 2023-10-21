package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface UserRepository {

    List<User> get(UserFilterOptions userFilterOptions);
    //potential filtering
    User getById(int id);

    List<User> getAll();

    User getByUsername(String username);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int id);
}
