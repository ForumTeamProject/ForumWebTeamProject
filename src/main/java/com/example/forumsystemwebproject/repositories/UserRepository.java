package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.UserFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;

import java.util.List;

public interface UserRepository {

    List<RegisteredUser> get(UserFilterOptions userFilterOptions);
    //potential filtering
    RegisteredUser getById(int id);

    List<RegisteredUser> getAll();

    void create(RegisteredUser user);

    void update(RegisteredUser user);

    void delete(RegisteredUser user);
}
