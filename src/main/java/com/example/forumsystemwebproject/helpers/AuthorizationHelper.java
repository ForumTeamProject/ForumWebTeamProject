package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;

public interface AuthorizationHelper {

    void creatorCheck(User user, Post post);

    void creatorCheck(User user, Comment comment);

    void creatorCheck(User user, PhoneNumber number);

    void creatorCheck(User userToCheck, User authenticatedUser);

    void adminCheck(User user);

    void blockedCheck(User user);
}
