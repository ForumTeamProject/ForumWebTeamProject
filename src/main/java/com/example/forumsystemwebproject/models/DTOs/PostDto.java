package com.example.forumsystemwebproject.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostDto {

    @NotNull(message = "Title can't be empty!")
    @Size(min = 16, max = 64, message = "Title must be between 16 to 64 characters long!")
    private String title;

    @NotNull(message = "Content can't be empty!")
    @Size(min = 32, max = 8192, message = "Content must be between 32 to 8192 characters long!")
    private String content;

    private String photoUrl;

    public PostDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
