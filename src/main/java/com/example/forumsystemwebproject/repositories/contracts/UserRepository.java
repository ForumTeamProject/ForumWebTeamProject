package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.User;
import org.hibernate.Session;

import java.util.List;

public interface UserRepository {

    User getById(int id);

    List<User> get(UserFilterOptions filterOptions);

    User getByUsername(String username);

    long getCount();

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(User userToDelete, User deletedUser);
}
