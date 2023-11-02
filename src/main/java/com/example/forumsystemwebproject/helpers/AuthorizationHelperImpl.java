package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelperImpl implements AuthorizationHelper{
    public static final String UNAUTHORIZED_MSG = "%s with %s %s is unauthorized to do this operation!";
    public static final String BLOCKED_USER_ROLE = "blockedUser";
    public static final String ADMIN_ROLE = "admin";
    private final RoleRepository roleRepository;

    @Autowired
    public AuthorizationHelperImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void authorizeUser(User userToAuthorize, Post post) {
        if (isBlockedUser(userToAuthorize) || (!isAdmin(userToAuthorize) && !isCreator(userToAuthorize, post))) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_MSG, "User", "username", userToAuthorize.getUsername()));
        }
    }

    @Override
    public void authorizeUser(User userToAuthorize, Comment comment) {
        if (isBlockedUser(userToAuthorize) || (!isAdmin(userToAuthorize) && !isCreator(userToAuthorize, comment))) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_MSG, "User", "username", userToAuthorize.getUsername()));
        }
    }

    @Override
    public boolean isCreator(User user, Post post) {
        return user.getId() == post.getUser().getId();
    }

    @Override
    public boolean isCreator(User user, Comment comment) {
        return user.getId() == comment.getUser().getId();
    }

    @Override
    public boolean isCreator(User user, PhoneNumber number) {
        return user.getId() == number.getUser().getId();
    }

    @Override
    public boolean isCreator(User userToCheck, User authenticatedUser) {
        return userToCheck.getId() == authenticatedUser.getId();
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getRoles().contains(roleRepository.getByName(ADMIN_ROLE));
    }

    @Override
    public boolean isBlockedUser(User userToBlock) {
        return userToBlock.getRoles().contains(roleRepository.getByName(BLOCKED_USER_ROLE));
    }
}
