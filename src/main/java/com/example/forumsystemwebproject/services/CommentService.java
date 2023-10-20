package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.helpers.CommentFilterOptions;
import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;

import java.util.List;

public interface CommentService {

    List<Comment> get(CommentFilterOptions filterOptions);

    List<Comment> getByUserId(CommentFilterOptions filterOptions, int id);

    Comment getById(int id);

    void create(Comment comment, int id);

    void update(Comment comment, RegisteredUser user);

    void delete(int id, RegisteredUser user);
}
