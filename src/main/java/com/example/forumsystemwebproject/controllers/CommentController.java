package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.CommentMapper;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.DTOs.CommentDto;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService service;

    private final CommentMapper mapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentController(CommentService service, CommentMapper mapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.mapper = mapper;
        this.authenticationHelper = authenticationHelper;
    }

//    @GetMapping("/comments")
//    public List<Comment> get(
//            @RequestParam(required = false) String user,
//            @RequestParam(required = false) String content,
//            @RequestParam(required = false) String sortBy,
//            @RequestParam(required = false) String sortOrder,
//            @RequestHeader HttpHeaders headers) {
//        try {
//            authenticationHelper.tryGetUser(headers);
//            CommentFilterOptions filterOptions = new CommentFilterOptions(user, content, sortBy, sortOrder);
//            return service.get(filterOptions);
//        }  catch (UnauthorizedOperationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }

    @GetMapping("/users/{id}/comments")
    public List<Comment> getByUserId(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @PathVariable int id,
            @RequestHeader HttpHeaders headers
    ) {
        try {
            authenticationHelper.tryGetUser(headers);
            CommentFilterOptions filterOptions = new CommentFilterOptions(null, content, sortBy, sortOrder);
            return service.getByUserId(filterOptions, id);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/posts/{id}/comments")
    public List<Comment> getByPostId(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id
    ) {
        try {
            authenticationHelper.tryGetUser(headers);
            return service.getByPostId(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @GetMapping("/comments/{id}")
    public Comment getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("posts/{id}/comments")
    public void create(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody CommentDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment commentToCreate = mapper.fromDto(dto);
            commentToCreate.setUser(user);
            service.create(commentToCreate, id);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/comments/{id}")
    public void update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody CommentDto dto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Comment commentToUpdate = mapper.fromDto(id, dto);
            service.update(commentToUpdate, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/comments/{id}")
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
