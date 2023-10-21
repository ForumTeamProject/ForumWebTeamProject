package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, RoleRepository roleRepository) {
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Post> get(PostFilterOptions filterOptions) {
        return postRepository.get(filterOptions);
    }

    @Override
    public List<Post> getByUserId(PostFilterOptions filterOptions, int id) {
        return postRepository.getByUserId(filterOptions, id);
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void create(Post post, User user) {
            post.setUser(user);
            postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        if (user.getRoles().contains(roleRepository.getByName("admin")) || post.getUser() == user) {
            postRepository.update(post);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to edit this post!");
        }
    }

    @Override
    public void delete(Post post, User user) {
        if (user.getRoles().contains(roleRepository.getByName("admin")) || post.getUser().getId() == user.getId()) {
            postRepository.delete(post);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to delete this post!");
        }
    }
}
