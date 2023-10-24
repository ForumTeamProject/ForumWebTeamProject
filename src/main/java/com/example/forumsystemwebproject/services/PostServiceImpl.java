package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.LikeService;
import com.example.forumsystemwebproject.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final RoleRepository roleRepository;

    private final LikeService likeService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, RoleRepository roleRepository, LikeService likeService) {
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.likeService = likeService;
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
    public void likePost(int id, User user) {
        Post post = postRepository.getById(id);
        try{
            Like like = likeService.get(post, user);
            likeService.delete(like);
        } catch (EntityNotFoundException e) {
            likeService.create(post, user);
        }
    }

    @Override
    public void create(Post post, User user) {
            post.setUser(user);
            postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        if (checkPermission(post, user)) {
            postRepository.update(post);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to edit this post!");
        }
    }



    @Override
    public void delete(int id, User user) {
        Post postToDelete = getById(id);
        if (checkPermission(postToDelete, user)) {
            postRepository.delete(postToDelete);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to delete this post!");
        }
    }

    private boolean checkPermission(Post post, User user) {
        return user.getRoles().contains(roleRepository.getByName("admin")) || post.getUser() == user;
    }
}
