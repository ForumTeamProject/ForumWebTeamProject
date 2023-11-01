package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.mappers.PhoneNumberMapper;
import com.example.forumsystemwebproject.models.DTOs.PhoneNumberDto;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.PhoneNumberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PhoneNumberController {

    private final PhoneNumberService service;

    private final AuthenticationHelper authenticationHelper;

    private final PhoneNumberMapper mapper;


    @Autowired
    public PhoneNumberController(PhoneNumberService service, AuthenticationHelper authenticationHelper, PhoneNumberMapper mapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.mapper = mapper;
    }

    @GetMapping("/phone-numbers")
    public List<PhoneNumber> get(@RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return service.get();
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/phone-numbers/{id}")
    public PhoneNumber getById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/phone-numbers")
    public void create(@RequestHeader HttpHeaders headers, @Valid @RequestBody PhoneNumberDto dto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            PhoneNumber numberToCreate = mapper.fromDto(dto);
            service.create(numberToCreate, authenticatedUser);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/phone-numbers/{id}")
    public void update(@RequestHeader HttpHeaders headers, @Valid @RequestBody PhoneNumberDto dto, @PathVariable int id) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            PhoneNumber numberToUpdate = mapper.fromDto(id, dto);
            service.update(numberToUpdate, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/phone-numbers/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            service.delete(id, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
