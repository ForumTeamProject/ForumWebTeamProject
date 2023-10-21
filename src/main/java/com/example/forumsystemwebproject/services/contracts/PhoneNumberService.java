package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface PhoneNumberService {

    List<PhoneNumber> get();

    PhoneNumber getById(int id);

    void create(PhoneNumber number, User authenticatedUser);

    void update(PhoneNumber number, User authenticatedUser);

    void delete(int id, User authenticatedUser);
}
