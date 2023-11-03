package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Tag;

import java.util.List;

public interface PostRepository {

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByUserId(PostFilterOptions filterOptions, int id);

    List<Post> getMostCommented();

    List<Post> getMostRecentlyCreatedPosts();

    Post getById(int id);

    long getCount();

    void create(Post post);

    void update(Post post);

    void delete(Post post);
}
