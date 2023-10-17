package com.example.forumsystemwebproject.models.UserModels;

import com.example.forumsystemwebproject.models.Role;

public interface User {
    int getId();

    void setId(int id);
    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);

    Role getRole();
    void setRole(Role role);

}


