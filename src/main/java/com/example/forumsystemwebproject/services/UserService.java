package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.UserFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.models.UserModels.UserDto;

import java.util.List;

public interface UserService {

    List<RegisteredUser> get(UserFilterOptions userFilterOptions);
    RegisteredUser getById(int id);

    List<RegisteredUser> getAll();

    void create(RegisteredUser user);

    void update(RegisteredUser userToUpdate, RegisteredUser authenticatedUser);

    void delete(int id);
}
