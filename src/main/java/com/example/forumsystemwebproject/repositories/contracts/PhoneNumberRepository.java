package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.models.PhoneNumber;

import java.util.List;

public interface PhoneNumberRepository {

    List<PhoneNumber> get();

    PhoneNumber getById(int id);

    void create(PhoneNumber number);

    void update(PhoneNumber number);

    void delete(PhoneNumber number);



}
