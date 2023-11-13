package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import com.example.forumsystemwebproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final RoleRepository roleRepository;

    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public List<User> get(UserFilterOptions filterOptions) {
        return repository.get(filterOptions);
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
        authorizationHelper.creatorCheck(userToUpdate, authenticatedUser);
        checkEmailUniqueness(userToUpdate);
        repository.update(userToUpdate);
    }

    @Override
    public void delete(User user, int id) {
        User userToDelete = getById(id);
        User deletedUser = getById(999);
        authorizationHelper.creatorCheck(user, userToDelete);

        repository.delete(userToDelete, deletedUser);
    }

    @Override
    public void blockOrUnblockUser(User user, User userToUpdate) {
        try {
            authorizationHelper.blockedCheck(userToUpdate);
            userToUpdate.getRoles().add(roleRepository.getByName("blockedUser"));
            repository.update(userToUpdate);
        } catch (UnauthorizedOperationException e) {
            for (Role role : userToUpdate.getRoles()) {
                if (role.getName().equals("blockedUser")) {
                    userToUpdate.getRoles().remove(role);
                    repository.update(userToUpdate);
                }
            }
        }
    }

//    public void addPhoneNumber(User user, PhoneNumber number) {
//
//    }

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
            User userToCheck = repository.getByEmail(user.getEmail());
            if (userToCheck.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
    }
}
