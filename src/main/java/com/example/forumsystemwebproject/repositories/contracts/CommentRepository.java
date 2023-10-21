package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.Post;

import java.util.List;

public interface CommentRepository {

//    List<Comment> get(CommentFilterOptions filterOptions);

    List<Comment> getByUserId(CommentFilterOptions filterOptions, int id);

    List<Comment> getByPostId(Post post);

    Comment getById(int id);

    void create(Comment comment);

    void update(Comment comment);

    void delete(Comment comment);
}
