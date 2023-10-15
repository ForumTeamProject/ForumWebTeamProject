package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.FilterOptions;
import com.example.forumsystemwebproject.helpers.PostMapper;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.PostDto;
import com.example.forumsystemwebproject.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;

    private final PostMapper mapper;

    @Autowired
    public PostController(PostService service, PostMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Post> get(
        @RequestParam(required = false) String user,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String sortOrder){
        FilterOptions filterOptions = new FilterOptions(user,title,sortBy,sortOrder);
        return service.get(filterOptions);
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

//    @PostMapping
//    public void create(@Valid @RequestBody PostDto dto) {
//        Post postToCreate = mapper.fromDto(dto);
//        try {
//            return service.create(postToCreate, //TODO user must be here);
//            )
//        }
//    }

}
