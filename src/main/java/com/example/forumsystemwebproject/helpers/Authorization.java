//package com.example.forumsystemwebproject.helpers;
//
//import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
//import com.example.forumsystemwebproject.models.Role;
//import com.example.forumsystemwebproject.models.User;
//import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public interface Authorization {
//
//
//    static void authorizeUser(User userToAuthorize, List<Role> roles){
//        for (Role role : roles) {
//            if (!(userToAuthorize.getRoles().contains(role))) {
//                throw new UnauthorizedOperationException(String.format(AuthorizationHelper.UNAUTHORIZED_MSG, "User", "username", userToAuthorize.getUsername()));
//            }
//        }
//    }
//    static List<Role> makeRoleListFromArgs(String... roles) {
//        List<Role> roleList = new ArrayList<>();
//        for (String role : roles) {
//            roleList.add(AuthorizationHelper.roleRepository.getByName(role));
//        }
//        return roleList;
//    }
//
//    static boolean isAdmin(User user) {
//        return user.getRoles().contains(AuthorizationHelper.admin);
//    }
//}
