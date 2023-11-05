package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.models.*;

import java.util.List;

public interface AuthorizationHelper {


    void authorizeUser(User userToAuthorize, Post post);

    void authorizeUser(User userToAuthorize, Comment comment);

    void creatorCheck(User user, Post post);

    void creatorCheck(User user, Comment comment);

    void creatorCheck(User user, PhoneNumber number);

    void creatorCheck(User userToCheck, User authenticatedUser);

    void adminCheck(User user);

    void blockedCheck(User user);
}
