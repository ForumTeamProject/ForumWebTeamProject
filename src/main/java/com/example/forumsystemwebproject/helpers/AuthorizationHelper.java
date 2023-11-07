package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.models.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AuthorizationHelper {
    void authorizeUser(User userToAuthorize, Post post);
    void authorizeUser(User userToAuthorize, Comment comment);
    boolean isCreator(User user, Post post);
    boolean isCreator(User user, Comment comment);

    boolean isCreator(User user, PhoneNumber number);

    boolean isCreator(User userToCheck, User authenticatedUser);

    boolean isAdmin(User user);

    boolean isBlockedUser(User userToBlock);
}
