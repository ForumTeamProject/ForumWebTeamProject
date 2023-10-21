//package com.example.forumsystemwebproject.helpers;
//
//import com.example.forumsystemwebproject.models.RegisteredUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AuthenticationHelper {
//
//    private UserService userService;
//
//    @Autowired
//    public AuthenticationHelper(UserService userService) {
//        this.userService = userService;
//    }
//
//    public static RegisteredUser tryGetUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String username = userDetails.getUsername();
//            return userService.getByUsername(username);
//        } else {
//            throw new UnsupportedOperationException("You must be logged into your account in order to proceed!");
//        }
//    }
//}

package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.getByUsername(username);

            if (!user.getPassword().equals(password)) {
                throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpace + 1);
    }
}

