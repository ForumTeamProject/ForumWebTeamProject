package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> get(PostFilterOptions postFilterOptions) {
        return postRepository.get(postFilterOptions);
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void create(Post post, RegisteredUser user) {
            post.setUser(user);
            postRepository.create(post);
    }

    @Override
    public void update(Post post, RegisteredUser user) {
        if (user.getRole().toString().equals("admin") || post.getUser() == user) {
            postRepository.update(post);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to edit this post!");
        }
    }

    @Override
    public void delete(Post post, RegisteredUser user) {
        if (user.getRole().toString().equalsIgnoreCase("admin") || post.getUser().getUsername().equalsIgnoreCase(user.getUsername())) {
            postRepository.delete(post);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to delete this post!");
        }
    }
}
