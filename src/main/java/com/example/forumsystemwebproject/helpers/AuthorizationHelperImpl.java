package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.models.*;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelperImpl implements AuthorizationHelper {
    public static final String UNAUTHORIZED_MSG = "%s with %s %s is unauthorized to do this operation!";
    public static final String BLOCKED_USER_ROLE = "blockedUser";
    public static final String ADMIN_ROLE = "admin";
    private final RoleRepository roleRepository;

    @Autowired
    public AuthorizationHelperImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void creatorCheck(User user, Post post) {
        if (user.getId() != post.getUser().getId()) {
            throw new UnauthorizedOperationException(String.format("%s with %s %s is unauthorized to do this operation!", "User", "username", user.getUsername()));

        }
    }

    @Override
    public void creatorCheck(User user, Comment comment) {
        if (user.getId() != comment.getUser().getId()) {
            throw new UnauthorizedOperationException(String.format("%s with %s %s is unauthorized to do this operation!", "User", "username", user.getUsername()));
        }
    }

    @Override
    public void creatorCheck(User user, PhoneNumber number) {
        if (user.getId() != number.getUser().getId()) {
            throw new UnauthorizedOperationException(String.format("%s with %s %s is unauthorized to do this operation!", "User", "username", user.getUsername()));
        }
    }

    @Override
    public void creatorCheck(User authenticatedUser, User userToCheck) {
        if (userToCheck.getId() != authenticatedUser.getId()) {
            throw new UnauthorizedOperationException(String.format("%s with %s %s is unauthorized to do this operation!", "User", "username", authenticatedUser.getUsername()));
        }
    }

    @Override
    public void adminCheck(User user) {
        for (Role role : user.getRoles()) {
            if (role.getName().equals(roleRepository.getByName(ADMIN_ROLE).getName())) {
                return;
            }
        }
        throw new UnauthorizedOperationException(String.format("%s with %s %s is unauthorized to do this operation!", "User", "username", user.getUsername()));
    }

    @Override
    public void blockedCheck(User user) {
        for (Role role : user.getRoles()) {
            if (role.getName().equals(roleRepository.getByName(BLOCKED_USER_ROLE).getName())) {
                throw new UnauthorizedOperationException(String.format("%s with %s %s is blocked and therefore unauthorized to do this operation!", "User", "username", user.getUsername()));
            }
        }
    }


}
