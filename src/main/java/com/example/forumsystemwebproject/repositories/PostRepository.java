package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> get(PostFilterOptions postFilterOptions);

    Post getById(int id);

    void create(Post post);

    void update(Post post);

    void delete(Post post);
}
