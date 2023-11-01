package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PhoneNumberRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {
    private final List<Role> authorizationRoles = AuthorizationHelper.makeRoleListFromArgs("admin");

    private final PhoneNumberRepository repository;

    private final RoleRepository roleRepository;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository repository, RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<PhoneNumber> get() {
        return repository.get();
    }

    @Override
    public PhoneNumber getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(PhoneNumber number, User authenticatedUser) {
//            if (!authenticatedUser.getRoles().contains(roleRepository.getByName("admin"))) {
//                throw new UnauthorizedOperationException("You do not have permission to set a phone number!");
//            }
        AuthorizationHelper.authorizeUser(authenticatedUser, authorizationRoles);
        number.setUser(authenticatedUser);
        checkNumberUniqueness(number);
        repository.create(number);
    }


    @Override
    public void update(PhoneNumber number, User authenticatedUser) {
        if (number.getUser().getId() != authenticatedUser.getId()) {
            throw new UnauthorizedOperationException("You do not have permission to change this phone number!");
        }
        checkNumberUniqueness(number);
        repository.update(number);
    }

    @Override
    public void delete(int id, User authenticatedUser) {
        PhoneNumber number = getById(id);
        if (number.getUser().getId() != authenticatedUser.getId()) {
            throw new UnauthorizedOperationException("You do not have permission to delete this phone number!");
        }
        repository.delete(number);
    }

    private void checkNumberUniqueness(PhoneNumber number) {
        boolean duplicateExists = true;

        try {
            PhoneNumber repositoryNumber = getById(number.getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Phone Number", "value:", number.toString());
        }
    }
}
