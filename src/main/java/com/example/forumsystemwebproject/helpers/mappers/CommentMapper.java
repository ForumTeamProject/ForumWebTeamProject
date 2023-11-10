package com.example.forumsystemwebproject.helpers.mappers;

import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.DTOs.CommentDto;
import com.example.forumsystemwebproject.services.contracts.CommentService;
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

    public CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setContent(comment.getContent());
        return dto;
    }
}
