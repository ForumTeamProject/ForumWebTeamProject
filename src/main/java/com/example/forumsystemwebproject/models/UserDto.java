package com.example.forumsystemwebproject.models;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class UserDto {

    @NotNull(message = "Username can't be empty")
    private String username;
    @NotNull(message = "Username can't be empty")
    //Password must contain one digit from 1 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be 8-16 characters long.
    @Pattern(regexp = "/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$/\n")
    private String password;
    @NotNull(message = "First name can't be empty")
    @Size(min = 4, max = 32, message = "Fist name should be between 2 and 30 symbols")
    private String firstName;
    @NotNull(message = "Last name can't be empty")
    @Size(min = 4, max = 32, message = "Last name should be between 2 and 30 symbols")
    private String lastName;

    @NotNull
    private String email;

    //TODO to add a regex validation for the email
    public UserDto() {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
