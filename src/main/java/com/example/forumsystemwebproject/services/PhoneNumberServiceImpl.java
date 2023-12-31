package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PhoneNumberRepository;
import com.example.forumsystemwebproject.services.contracts.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository repository;

    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository repository, AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;
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
        authorizationHelper.adminCheck(authenticatedUser);
        authorizationHelper.blockedCheck(authenticatedUser);
        number.setUser(authenticatedUser);
        checkNumberUniqueness(number);
        repository.create(number);
    }

    @Override
    public void update(PhoneNumber number, User authenticatedUser) {
        authorizationHelper.creatorCheck(authenticatedUser, number);
        checkNumberUniqueness(number);
        repository.update(number);
    }

    @Override
    public void delete(int id, User authenticatedUser) {
        PhoneNumber number = getById(id);
        authorizationHelper.creatorCheck(authenticatedUser, number);
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
