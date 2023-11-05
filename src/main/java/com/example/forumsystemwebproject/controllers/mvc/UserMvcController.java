package com.example.forumsystemwebproject.controllers.mvc;

import com.example.forumsystemwebproject.exceptions.AuthenticationFailureException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.PaginationHelper;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.DTOs.UserFilterDto;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final RoleRepository roleRepository;

    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public UserMvcController(RoleRepository roleRepository, UserService userService, AuthenticationHelper authenticationHelper, AuthorizationHelper authorizationHelper) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.authorizationHelper = authorizationHelper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            User user = (User) session.getAttribute("currentUser");
            for (Role role: user.getRoles()) {
                if (role.getId() == roleRepository.getByName("admin").getId()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showUsers(HttpSession session, @ModelAttribute("filterOptions") UserFilterDto filterDto, Model model,
                            @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size) {
        User user;
        try {
          user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            UserFilterOptions userFilterOptions = new UserFilterOptions(
                    filterDto.getUsername(),
                    filterDto.getEmail(),
                    filterDto.getFirstName(),
                    filterDto.getLastName(),
                    filterDto.getSortBy(),
                    filterDto.getSortOrder()
            );
            List<User> users = userService.get(userFilterOptions);
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(10);

            Page<User> userPage = PaginationHelper.findPaginated(PageRequest.of(currentPage - 1, pageSize), users);

            model.addAttribute("userPage", userPage);

            int totalPages = userPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }   model.addAttribute("users", users);
            model.addAttribute("filterOptions", filterDto);
            return "UsersView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "AccessDenied";
        }
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable int id, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            authorizationHelper.adminCheck(user);
            User userToShow = userService.getById(id);
            model.addAttribute("user", userToShow);
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("notFound", e.getMessage());
            return "NotFound";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("unauthorized", e.getMessage());
            return "АccessDenied";
        }
    }

    @PostMapping("/{id}/blockOrUnblock")
    public String blockOrUnblockUser(@PathVariable int id, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            authorizationHelper.adminCheck(user);
            authorizationHelper.blockedCheck(user);
            User userToUpdate = userService.getById(id);
            userService.blockOrUnblockUser(user, userToUpdate);
            model.addAttribute("isBlocked", isBlocked(userToUpdate));
            model.addAttribute("user", userToUpdate);
            return "UserView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("unauthorized", e.getMessage());
            return "AccessDenied";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("notFound", e.getMessage());
            return "NotFound";
        }
    }

    private boolean isBlocked(User user) {
        try {
            authorizationHelper.blockedCheck(user);
            return false;
        } catch (UnauthorizedOperationException e) {
            return true;
        }
    }

}
