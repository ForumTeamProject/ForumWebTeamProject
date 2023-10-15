package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.helpers.FilterOptions;
import com.example.forumsystemwebproject.models.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {
    @Override
    public List<Post> get(FilterOptions filterOptions) {
        return null;
    }

    @Override
    public Post getById(int id) {
        return null;
    }

    @Override
    public void create(Post post) {

    }

    @Override
    public void update(Post post) {

    }

    @Override
    public void delete(int id) {

    }
}
