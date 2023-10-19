package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.CommentFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.repositories.CommentRepository;
import com.example.forumsystemwebproject.repositories.PostRepository;
import com.example.forumsystemwebproject.repositories.PostRepositoryImpl;
import com.example.forumsystemwebproject.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final PostRepository postRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, PostRepository postRepository, RoleRepository roleRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Comment> get(CommentFilterOptions filterOptions) {
        return repository.get(filterOptions);
    }

    @Override
    public List<Comment> getByUserId(CommentFilterOptions filterOptions, int id) {
        return repository.getByUserId(filterOptions,id);
    }

    @Override
    public Comment getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Comment comment, int id) {
            Post post = postRepository.getById(id);
            comment.setPost(post);
            repository.create(comment);
    }

    @Override
    public void update(Comment comment, RegisteredUser user) {
            if (user.getId() != comment.getUser().getId()) {
                throw new UnauthorizedOperationException("You do not have permission to edit this comment!");
            } else {
                repository.update(comment);
            }
    }

    @Override
    public void delete(int id, RegisteredUser user) {
//        Comment commentToDelete = getById(id);
//        if (user.getId() != commentToDelete.getUser().getId() || !roleRepository.getRoles(user).contains("admin")) {
//            throw new UnauthorizedOperationException("You do not have permission to delete this comment!");
//        } else {
//            repository.delete(commentToDelete);
//        }
    }
}
