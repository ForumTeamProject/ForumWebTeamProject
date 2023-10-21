package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.UserMapper;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.models.DTOs.UserDto;
import com.example.forumsystemwebproject.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserMapper mapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService userService, UserMapper mapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.mapper = mapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> get(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestHeader HttpHeaders headers) {
        try {
            UserFilterOptions userFilterOptions = new UserFilterOptions(username, email, firstName, lastname, sortBy, sortOrder);
            authenticationHelper.tryGetUser(headers);
            return userService.get(userFilterOptions);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/register")
    public void create(@Valid @RequestBody UserDto dto) {
        try {
            User newUser = mapper.fromDto(dto);
            userService.create(newUser);
        } catch (DuplicateEntityException e ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserDto dto, @PathVariable int id) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            User userToUpdate = mapper.fromDto(id, dto);
            userService.update(userToUpdate, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    //TODO probably add something like users/{id}/block and on this endpoint an admin can block a user

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id)  {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.delete(user, id);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,  e.getMessage());
        } catch (EntityNotFoundException e ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }


    }

}


