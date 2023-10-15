package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.helpers.FilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.RegisteredUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Override
    public List<Post> get(FilterOptions filterOptions) {
        return null;
    }

    @Override
    public Post getById(int id) {
        return null;
    }

    @Override
    public void create(Post post, RegisteredUser user) {

    }

    @Override
    public void update(Post post, RegisteredUser user) {

    }

    @Override
    public void delete(int id, RegisteredUser user) {

    }
}
