package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorizationHelper {
    private static final String UNAUTHORIZED_MSG = "%s with %s %s is unauthorized to do this operation!";
    private static RoleRepository roleRepository;
    private static Role admin = roleRepository.getByName("admin");

    @Autowired
    public AuthorizationHelper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static void authorizeUser(User userToAuthorize, List<Role> roles) {
        for (Role role : roles) {
            if (!(userToAuthorize.getRoles().contains(role))) {
                throw new UnauthorizedOperationException(String.format(AuthorizationHelper.UNAUTHORIZED_MSG, "User", "username", userToAuthorize.getUsername()));
            }
        }
    }

    public static List<Role> makeRoleListFromArgs(String... roles) {
        List<Role> roleList = new ArrayList<>();
        for (String role : roles) {
            roleList.add(AuthorizationHelper.roleRepository.getByName(role));
        }
        return roleList;
    }

    public static boolean isAdmin(User user) {
        return user.getRoles().contains(AuthorizationHelper.admin);
    }
}


//throw new UnauthorizedOperationException(
//                        String.format(UNAUTHORIZED_MSG, "User", "username", userToAuthenticate.getUsername()));
//