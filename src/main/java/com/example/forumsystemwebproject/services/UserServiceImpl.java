package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import com.example.forumsystemwebproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> get(UserFilterOptions userFilterOptions) {
        return null;
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User getByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }
    @Override
    public void create(User user) {
        checkUsernameUniqueness(user);
        checkEmailUniqueness(user);
        repository.create(user);
    }

    @Override
    public void update(User userToUpdate, User authenticatedUser) {
            if (userToUpdate.getId() != authenticatedUser.getId()) {
                throw new UnauthorizedOperationException("You do not have permission to change this user's details!");
            }
            checkUsernameUniqueness(userToUpdate);
            checkEmailUniqueness(userToUpdate);
            repository.update(userToUpdate);
    }

    @Override
    public void delete(User user, int id) {
        if (user.getId() != id) {
            throw new UnauthorizedOperationException("You do not have permission to delete this user!");
        }
        repository.delete(id);
    }

    private void checkUsernameUniqueness(User user) {
        boolean duplicateExists = true;
        try {
            repository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
    }

    private void checkEmailUniqueness(User user) {
        boolean duplicateExists = true;
        try {
            repository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
    }
}
