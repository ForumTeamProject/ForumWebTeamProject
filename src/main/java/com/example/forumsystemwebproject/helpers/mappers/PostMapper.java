package com.example.forumsystemwebproject.helpers.mappers;

import com.example.forumsystemwebproject.models.DTOs.PostDto;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        post.setTags(repositoryPost.getTags());
        post.setCreationDate(repositoryPost.getCreationDate());
        post.setLikes(repositoryPost.getLikes());
        return post;
    }

    public Post fromDto(PostDto dto) {
        Post post = new Post();
        Set<Tag> tags = new HashSet<>();
        Set<Like> likes = new HashSet<>();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setTags(tags);
        post.setLikes(likes);
        post.setCreationDate(new Date());
        return post;
    }

    public PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        return dto;
    }
}
