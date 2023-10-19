package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.CommentDto;
import com.example.forumsystemwebproject.services.CommentService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final CommentService service;

    @Autowired
    public CommentMapper(CommentService service) {
        this.service = service;
    }

    public Comment fromDto(int id, CommentDto dto) {
        Comment comment = fromDto(dto);
        comment.setId(id);
        Comment commentRepository = service.getById(id);
        comment.setUser(commentRepository.getUser());
        comment.setPost(commentRepository.getPost());
        return comment;
    }

    public Comment fromDto(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }
}
