package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.UserFilterOptions;
import com.example.forumsystemwebproject.helpers.UserMapper;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.models.UserModels.UserDto;
import com.example.forumsystemwebproject.services.UserService;
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
    public List<RegisteredUser> get(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestHeader HttpHeaders headers) {
        try {
            UserFilterOptions userFilterOptions = new UserFilterOptions(username, email, firstName, sortBy, sortOrder);
            authenticationHelper.tryGetUser(headers);
            return userService.get(userFilterOptions);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public RegisteredUser getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    //TODO Create User method could be created in a RegisterController as /users endpoint does not correspond to registering new user
    //TODO Delete user should be discussed as i am not sure whether it is possible in this context.
    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserDto dto, @PathVariable int id) {
        try {
            RegisteredUser authenticatedUser = authenticationHelper.tryGetUser(headers);
            RegisteredUser userToUpdate = mapper.fromDto(id, dto);
            userService.update(userToUpdate, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}

