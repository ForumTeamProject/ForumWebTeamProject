package com.example.forumsystemwebproject.controllers.rest;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.mappers.RoleMapper;
import com.example.forumsystemwebproject.models.DTOs.RoleDto;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles")
@SecurityRequirement(name = "basicAuth")
public class RoleController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public RoleController(RoleService roleService, RoleMapper roleMapper, AuthenticationHelper authenticationHelper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Role> get() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public Role getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return roleService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void create(@RequestHeader HttpHeaders headers, @Valid @RequestBody RoleDto roleDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Role newRole = roleMapper.fromDto(roleDto);
            roleService.create(newRole, user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers, @Valid @RequestBody RoleDto roleDto, @PathVariable int id) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Role roleToUpdate = roleMapper.fromDto(id, roleDto);
            roleService.update(roleToUpdate, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            roleService.delete(id, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
}