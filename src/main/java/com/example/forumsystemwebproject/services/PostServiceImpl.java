package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.*;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostTagRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import com.example.forumsystemwebproject.services.contracts.LikeService;
import com.example.forumsystemwebproject.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final List<Role> authorizationRoles = AuthorizationHelper.makeRoleListFromArgs("user");
    private final List<Role> authorizationRolesForDelete = AuthorizationHelper.makeRoleListFromArgs("user", "admin");

    private final PostRepository postRepository;

    private final RoleRepository roleRepository;

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    private final LikeService likeService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, RoleRepository roleRepository, LikeService likeService, TagRepository tagRepository, PostTagRepository postTagRepository) {
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
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

    public void addTagToPost(User userWhoAdds, Post post, Tag tag) {
        AuthorizationHelper.authorizeUser(userWhoAdds, authorizationRolesForDelete);

        if (post.getUser().getId() == userWhoAdds.getId() || AuthorizationHelper.isAdmin(userWhoAdds)) {
            postRepository.addTagToPost(post, tag);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to add this tag!");
        }
    }

    public void addTagsToPost(User userWhoAdds, Post post, List<Tag> tags) {
        AuthorizationHelper.authorizeUser(userWhoAdds, authorizationRolesForDelete);

        if (post.getUser().getId() == userWhoAdds.getId() || AuthorizationHelper.isAdmin(userWhoAdds)) {
            postRepository.addTagsToPost(post, tags);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to add these tags!");
        }
    }

    public void deleteTagFromPost(User userWhoDeletes, Post postFromWhichToDelete, Tag tag) {
        AuthorizationHelper.authorizeUser(userWhoDeletes, authorizationRolesForDelete);

        if (postFromWhichToDelete.getUser().getId() == userWhoDeletes.getId() || AuthorizationHelper.isAdmin(userWhoDeletes)) {
            postRepository.deletePostTagAssociation(postFromWhichToDelete, tag);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to remove this tag!");
        }
    }


}
