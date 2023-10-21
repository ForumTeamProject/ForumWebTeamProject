package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface PostService {

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByUserId(PostFilterOptions filterOptions, int id);

    Post getById(int id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(Post post, User user);
}
