package com.example.forumsystemwebproject.models.DTOs;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotNull(message = "Content can't be empty!")
    @Size(max = 8192, message = "The content cannot exceed 8192 characters!")
    private String content;

    public CommentDto() {

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
