package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface UserRepository {

    User getById(int id);

    List<User> getAll(UserFilterOptions filterOptions);

    User getByUsername(String username);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int id);
}
