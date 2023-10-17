package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.PostMapper;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.PostDto;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;

    private final PostMapper mapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostController(PostService service, PostMapper mapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.mapper = mapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Post> get(
        @RequestParam(required = false) String user,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String sortOrder,
        @RequestHeader HttpHeaders headers) {
        try {
            PostFilterOptions postFilterOptions = new PostFilterOptions(user, title, sortBy, sortOrder);
            authenticationHelper.tryGetUser(headers);
            return service.get(postFilterOptions);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void create(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto dto) {
        try {
        RegisteredUser user = authenticationHelper.tryGetUser(headers);
        Post postToCreate = mapper.fromDto(dto);
        service.create(postToCreate, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto dto, @PathVariable int id) {
        try {
            Post postToUpdate = mapper.fromDto(id, dto);
            RegisteredUser user = authenticationHelper.tryGetUser(headers);
            service.update(postToUpdate, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            RegisteredUser user = authenticationHelper.tryGetUser(headers);
            Post postToDelete = getById(id);
            service.delete(postToDelete, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
