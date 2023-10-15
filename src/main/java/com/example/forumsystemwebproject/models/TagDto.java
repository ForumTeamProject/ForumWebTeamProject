package com.example.forumsystemwebproject.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagDto {

    @NotNull
    @Size(max = 20, message = "The tag must not be over 20 characters long!")
    private String content;

    public TagDto() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
