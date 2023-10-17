package com.example.forumsystemwebproject.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserToRegisterDto implements User {

    @NotNull(message = "Username can't be empty!")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters long!")
    private String username;
    @NotNull(message = "Password can't be empty!")
    @Size(min = 5, message = "Password must be more than 5 characters long!")
    //Password must contain one digit from 1 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be 8-16 characters long.
    @Pattern(regexp = "/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$/\n")
    private String password;
    @NotNull(message = "First name can't be empty!")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 characters long!")
    private String firstName;
    @NotNull(message = "Last name can't be empty!")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 characters long!")
    private String lastName;

    @NotNull(message = "Email can't be empty!")
    @Email
    private String email;

    public UserToRegisterDto() {
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

}
