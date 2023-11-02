package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface CommentService {

    List<Comment> get(CommentFilterOptions filterOptions);

    List<Comment> getByUserId(CommentFilterOptions filterOptions, int id);

    List<Comment> getByPostId(int id);

    Comment getById(int id);

    void create(Comment comment, int id, User user);

    void update(Comment comment, User user);

    void delete(int id, User user);
}
