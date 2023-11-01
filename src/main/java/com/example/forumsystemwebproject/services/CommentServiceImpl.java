package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.CommentRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final List<Role> authorizationRoles = AuthorizationHelper.makeRoleListFromArgs("user");
    private final List<Role> authorizationRolesForDelete = AuthorizationHelper.makeRoleListFromArgs("user", "admin");

    private final CommentRepository repository;

    private final PostRepository postRepository;


    private final RoleRepository roleRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, PostRepository postRepository, RoleRepository roleRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
    }

//    @Override
//    public List<Comment> get(CommentFilterOptions filterOptions) {
//        return repository.get(filterOptions);
//    }

    @Override
    public List<Comment> getByUserId(CommentFilterOptions filterOptions, int id) {
        return repository.getByUserId(filterOptions, id);
    }

    @Override
    public List<Comment> getByPostId(int id) {
        Post post = postRepository.getById(id);
        return repository.getByPostId(post);
    }

    @Override
    public Comment getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Comment comment, int id) {
        AuthorizationHelper.authorizeUser(comment.getUser(), authorizationRoles);
        Post post = postRepository.getById(id);
        comment.setPost(post);
        repository.create(comment);
    }

    @Override
    public void update(Comment comment, User user) {
        AuthorizationHelper.authorizeUser(user, authorizationRoles);
        if (user.getId() != comment.getUser().getId()) {
            throw new UnauthorizedOperationException("You do not have permission to edit this comment!");
        } else {
            repository.update(comment);
        }
    }

    @Override
    public void delete(int id, User user) {
        AuthorizationHelper.authorizeUser(user, authorizationRolesForDelete);

        Comment commentToDelete = getById(id);
        if (user.getId() != commentToDelete.getUser().getId()) {
            throw new UnauthorizedOperationException("You do not have permission to delete this comment!");
        } else {
            repository.delete(commentToDelete);
        }
    }
}
