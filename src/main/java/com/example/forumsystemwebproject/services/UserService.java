package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.helpers.UserFilterOptions;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;

import java.util.List;

public interface UserService {

    List<RegisteredUser> get(UserFilterOptions userFilterOptions);
    RegisteredUser getById(int id);

    List<RegisteredUser> getAll();

    RegisteredUser getByUsername(String username);

    RegisteredUser getByEmail(String email);

    void create(RegisteredUser user);

    void update(RegisteredUser userToUpdate, RegisteredUser authenticatedUser);

    void delete(RegisteredUser user, int id);
}
