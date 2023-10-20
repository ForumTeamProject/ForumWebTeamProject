package com.example.forumsystemwebproject.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoleDto {
    @NotNull(message = "Content can't be empty!")
    @Size(min = 32, max = 8192, message = "Content must be between 32 to 8192 characters long!")
    private String name;

    public RoleDto(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
