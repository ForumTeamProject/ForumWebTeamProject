package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.AuthorizationHelperImpl;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.*;
import com.example.forumsystemwebproject.repositories.TagRepositoryImpl;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostTagRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import com.example.forumsystemwebproject.services.contracts.LikeService;
import com.example.forumsystemwebproject.services.contracts.PostService;
import com.example.forumsystemwebproject.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final AuthorizationHelper authorizationHelper;
    private final PostRepository postRepository;
    private final LikeService likeService;

    private final TagService tagService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, LikeService likeService, AuthorizationHelper authorizationHelper, TagService tagService) {
        this.postRepository = postRepository;
        this.likeService = likeService;
        this.authorizationHelper = authorizationHelper;
        this.tagService = tagService;
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
    public List<Post> getMostCommented() {
        return postRepository.getMostCommented();
    }

    @Override
    public List<Post> getMostRecentlyCreatedPosts() {
        return postRepository.getMostRecentlyCreatedPosts();
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void likePost(int id, User user) {
        Post post = postRepository.getById(id);
        try {
            Like like = likeService.get(post, user);
            likeService.delete(like);
        } catch (EntityNotFoundException e) {
            likeService.create(post, user);
        }
    }

    @Override
    public void create(Post post, User user) {
        authorizationHelper.blockedCheck(user);
        post.setUser(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        authorizationHelper.blockedCheck(user);
        authorizationHelper.creatorCheck(user,post);
        postRepository.update(post);
    }

    @Override
    public void delete(int id, User user) {
        Post postToDelete = getById(id);
        authorizationHelper.authorizeUser(user, postToDelete);
        postRepository.delete(postToDelete);
    }

    public void addTagToPost(User userWhoAdds, Post post, Tag tag) {
        authorizationHelper.blockedCheck(userWhoAdds);
        authorizationHelper.creatorCheck(userWhoAdds, post);

        Tag newTag = tagService.create(tag, userWhoAdds);
        post.getTags().add(newTag);
        postRepository.update(post);
    }

    public void deleteTagFromPost(User userWhoDeletes, Post postFromWhichToDelete, Tag tag) {
        authorizationHelper.blockedCheck(userWhoDeletes);
        authorizationHelper.creatorCheck(userWhoDeletes, postFromWhichToDelete);
        postFromWhichToDelete.getTags().remove(tag);
        postRepository.update(postFromWhichToDelete);
    }
}
