package com.example.forumsystemwebproject.helpers.mappers;

import com.example.forumsystemwebproject.models.DTOs.PhoneNumberDto;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.services.contracts.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper {

   private PhoneNumberService service;

    @Autowired
    public PhoneNumberMapper(PhoneNumberService service) {
        this.service = service;
    }

    public PhoneNumber fromDto(int id, PhoneNumberDto dto) {
        PhoneNumber phoneNumber = fromDto(dto);
        phoneNumber.setId(id);
        PhoneNumber phoneNumberRepository = service.getById(id);
        phoneNumber.setUser(phoneNumberRepository.getUser());
        return phoneNumber;
    }

    public PhoneNumber fromDto(PhoneNumberDto dto) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(dto.getNumber());
        return phoneNumber;
    }
}
