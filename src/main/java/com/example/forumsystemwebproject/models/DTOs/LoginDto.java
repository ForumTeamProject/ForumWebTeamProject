package com.example.forumsystemwebproject.models.DTOs;

import jakarta.validation.constraints.*;

public class LoginDto {

    @NotEmpty(message = "Username can't be empty")
    public String username;

    @NotEmpty(message = "Password can't be empty")
    public String password;

    public LoginDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
