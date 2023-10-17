package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.UserFilterOptions;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.models.UserModels.UserDto;
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

    //TODO check user repository for more info -->
//    @Override
//    public RegisteredUser getByUsername(String username) {
//        return repository.getByUsername(username);
//    }
//
//    @Override
//    public RegisteredUser getByEmail(String email) {
//        return repository.getByEmail(email);
//    }
    @Override
    public void create(RegisteredUser user) {
    //TODO we have to ask whether the create user should be here.
    }

    @Override
    public void update(RegisteredUser userToUpdate,RegisteredUser authenticatedUser) {
            if (userToUpdate.getId() != authenticatedUser.getId()) {
                throw new UnauthorizedOperationException("You do not have permission to change this user's details!");
            }
            repository.update(userToUpdate);
    }

    @Override
    public void delete(int id) {

    }


}
