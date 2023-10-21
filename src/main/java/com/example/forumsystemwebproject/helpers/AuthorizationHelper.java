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

    @Autowired
    public AuthorizationHelper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static void authenticateUser(User userToAuthenticate, List<Role> roles) {
        for (Role role : roles) {
            if (!(userToAuthenticate.getRoles().contains(role))) {
                throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_MSG,"User", "username", userToAuthenticate.getUsername() ));
            }
        }
    }

    public static List<Role> makeRoleListFromArgs(String... roles) {
        List<Role> roleList = new ArrayList<>();
        for (String role : roles) {
            roleList.add(roleRepository.getByName(role));
        }
        return roleList;
    }
}


//throw new UnauthorizedOperationException(
//                        String.format(UNAUTHORIZED_MSG, "User", "username", userToAuthenticate.getUsername()));
//