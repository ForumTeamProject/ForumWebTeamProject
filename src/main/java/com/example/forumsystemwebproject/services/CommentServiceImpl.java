package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.AuthorizationHelperImpl;
import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.Post;
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

    private final CommentRepository repository;

    private final PostRepository postRepository;

    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, PostRepository postRepository, AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public List<Comment> get(CommentFilterOptions filterOptions) {
        return repository.get(filterOptions);
    }
    @Override
    public List<Comment> getByUserId(int id){
        return repository.getByUserId(id);
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
    public void create(Comment comment, int id, User user) {
        authorizationHelper.blockedCheck(user);
        Post post = postRepository.getById(id);
        comment.setPost(post);
        comment.setUser(user);
        repository.create(comment);
    }

    @Override
    public void update(Comment comment, User user) {
        authorizationHelper.blockedCheck(user);
        authorizationHelper.creatorCheck(user, comment);
        repository.update(comment);
    }

    @Override
    public void delete(int id, User user) {
        Comment commentToDelete = repository.getById(id);
        authorizationHelper.blockedCheck(user);
        authorizationHelper.creatorCheck(user, commentToDelete);
        repository.delete(commentToDelete);

//        Comment commentToDelete = getById(id);
//        if (user.getId() != commentToDelete.getUser().getId() || !roleRepository.getRoles(user).contains("admin")) {
//            throw new UnauthorizedOperationException("You do not have permission to delete this comment!");
//        } else {
//            repository.delete(commentToDelete);
//        }
    }
}
