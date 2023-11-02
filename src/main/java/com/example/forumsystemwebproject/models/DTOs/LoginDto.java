package com.example.forumsystemwebproject.models.DTOs;

import jakarta.validation.constraints.NotEmpty;

public class LoginDto {

    @NotEmpty
    public String username;

    @NotEmpty
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
