package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostTagRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
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

    @Autowired
    public PostServiceImpl(PostRepository postRepository, RoleRepository roleRepository, TagRepository tagRepository, PostTagRepository postTagRepository) {
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
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
        AuthorizationHelper.authorizeUser(user, authorizationRoles);
        post.setUser(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        AuthorizationHelper.authorizeUser(user, authorizationRoles);
        if (post.getUser() == user || AuthorizationHelper.isAdmin(user)) {
            postRepository.update(post);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to edit this post!");
        }
    }

    @Override
    public void delete(Post post, User user) {
        AuthorizationHelper.authorizeUser(user, authorizationRolesForDelete);

        if (post.getUser().getId() == user.getId() || AuthorizationHelper.isAdmin(user)) {
            postRepository.delete(post);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to delete this post!");
        }
    }
    public void addTagToPost(User userWhoAdds, Post post, Tag tag){
        AuthorizationHelper.authorizeUser(userWhoAdds, authorizationRolesForDelete);

        if (post.getUser().getId() == userWhoAdds.getId() || AuthorizationHelper.isAdmin(userWhoAdds)) {
           postRepository.addTagToPost(post, tag);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to add this tag!");
        }
    }
    public void addTagsToPost(User userWhoAdds, Post post, List<Tag> tags){
        AuthorizationHelper.authorizeUser(userWhoAdds, authorizationRolesForDelete);

        if (post.getUser().getId() == userWhoAdds.getId() || AuthorizationHelper.isAdmin(userWhoAdds)) {
            postRepository.addTagsToPost(post, tags);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to add these tags!");
        }
    }
    public void deleteTagFromPost(User userWhoDeletes,Post postFromWhichToDelete,Tag tag){
        AuthorizationHelper.authorizeUser(userWhoDeletes, authorizationRolesForDelete);

        if (postFromWhichToDelete.getUser().getId() == userWhoDeletes.getId() || AuthorizationHelper.isAdmin(userWhoDeletes)) {
            postRepository.deletePostTagAssociation(postFromWhichToDelete, tag);
        } else {
            throw new UnauthorizedOperationException("You do not have permission to remove this tag!");
        }
    }


}
