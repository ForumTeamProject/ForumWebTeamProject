package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.PostMapper;
import com.example.forumsystemwebproject.helpers.mappers.TagMapper;
import com.example.forumsystemwebproject.models.DTOs.PostDto;
import com.example.forumsystemwebproject.models.DTOs.TagDto;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.PostService;
import com.example.forumsystemwebproject.services.contracts.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    private final PostMapper mapper;
    private final TagMapper tagMapper;
    private final TagService tagService;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostController(PostService postService, PostMapper mapper, TagMapper tagMapper, TagService tagService, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.mapper = mapper;
        this.tagMapper = tagMapper;
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/posts")
    public List<Post> get(
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestHeader HttpHeaders headers) {
        try {
            PostFilterOptions filterOptions = new PostFilterOptions(user, title, sortBy, sortOrder);
            authenticationHelper.tryGetUser(headers);
            return postService.get(filterOptions);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> getByUserId(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @PathVariable int id,
            @RequestHeader HttpHeaders headers) {
        try {
            PostFilterOptions filterOptions = new PostFilterOptions(null, title, sortBy, sortOrder);
            authenticationHelper.tryGetUser(headers);
            return postService.getByUserId(filterOptions, id);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/posts/{id}")
    public Post getById(@PathVariable int id) {
        try {
            return postService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/posts")
    public void create(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post postToCreate = mapper.fromDto(dto);
            postService.create(postToCreate, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/posts/{id}")
    public void update(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto dto, @PathVariable int id) {
        try {
            Post postToUpdate = mapper.fromDto(id, dto);
            User user = authenticationHelper.tryGetUser(headers);
            postService.update(postToUpdate, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @DeleteMapping("/posts/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post postToDelete = getById(id);
            postService.delete(postToDelete, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    // POST_TAG STUFF*********
    @PatchMapping("posts/{id}/tags")
    public void addTagToPost(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody TagDto tagDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post postToAddTag = postService.getById(id);
            Tag tagToAdd = tagMapper.fromDto(tagDto);
            postService.addTagToPost(authenticatedUser, postToAddTag, tagToAdd);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("posts/{id}/tags")
    public void addTagsToPost(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody List<TagDto> tagsDtos) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post postToAddTag = postService.getById(id);
            List<Tag> tags = new ArrayList<>();
            for (TagDto dto : tagsDtos) {
                tags.add(tagMapper.fromDto(dto));
            }
            postService.addTagsToPost(authenticatedUser, postToAddTag, tags);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/posts/{id}/tags/{tag_id}")
    public void removeTagFromPost(@RequestHeader HttpHeaders headers, @PathVariable int id, @PathVariable int tag_id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post postFromWhichToDelete = getById(id);
            Tag tagToRemove = tagService.getById(tag_id);
            postService.deleteTagFromPost(user, postFromWhichToDelete, tagToRemove);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
