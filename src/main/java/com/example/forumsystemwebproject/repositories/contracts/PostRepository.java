package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByUserId(PostFilterOptions filterOptions, int id);

    Post getById(int id);

    void create(Post post);

    void update(Post post);

    void delete(Post post);
}