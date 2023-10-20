package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;

import java.util.List;

public interface PostService {

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByUserId(PostFilterOptions filterOptions, int id);

    Post getById(int id);

    void create(Post post, RegisteredUser user);

    void update(Post post, RegisteredUser user);

    void delete(Post post, RegisteredUser user);
}
