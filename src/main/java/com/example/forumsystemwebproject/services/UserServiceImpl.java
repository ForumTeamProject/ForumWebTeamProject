package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.UserFilterOptions;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RegisteredUser> get(UserFilterOptions userFilterOptions) {
        return null;
    }

    @Override
    public RegisteredUser getById(int id) {
        return repository.getById(id);
    }

    @Override
    public List<RegisteredUser> getAll() {
        return repository.getAll();
    }

    @Override
    public RegisteredUser getByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public RegisteredUser getByEmail(String email) {
        return repository.getByEmail(email);
    }
    @Override
    public void create(RegisteredUser user) {
        checkUsernameUniqueness(user);
        checkEmailUniqueness(user);
        repository.create(user);
    }

    @Override
    public void update(RegisteredUser userToUpdate,RegisteredUser authenticatedUser) {
            if (userToUpdate.getId() != authenticatedUser.getId()) {
                throw new UnauthorizedOperationException("You do not have permission to change this user's details!");
            }
            repository.update(userToUpdate);
    }

    @Override
    public void delete(RegisteredUser user, int id) {
        if (user.getId() != id) {
            throw new UnauthorizedOperationException("You do not have permission to delete this user!");
        }
        repository.delete(id);
    }

    private void checkUsernameUniqueness(RegisteredUser user) {
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

    private void checkEmailUniqueness(RegisteredUser user) {
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
