package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.PostDto;
import com.example.forumsystemwebproject.services.PostService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
public class PostMapper {

    private final PostService service;

    @Autowired
    public PostMapper(PostService service) {
        this.service = service;
    }

    public Post fromDto(int id, PostDto dto) {
        Post post = fromDto(dto);
        post.setId(id);
        Post repositoryPost = service.getById(id);
        post.setUser(repositoryPost.getUser());
        return post;
    }

    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return post;
    }
    //TODO we need to set the user of the post in the post service class
}
