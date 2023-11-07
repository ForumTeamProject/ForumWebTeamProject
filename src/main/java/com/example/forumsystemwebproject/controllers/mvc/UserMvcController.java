package com.example.forumsystemwebproject.controllers.mvc;

import com.example.forumsystemwebproject.exceptions.*;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.PaginationHelper;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.UserMapper;
import com.example.forumsystemwebproject.models.DTOs.UserDto;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
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

    private final UserMapper mapper;

    @Autowired
    public UserMvcController(RoleRepository roleRepository, UserService userService, AuthenticationHelper authenticationHelper, UserMapper mapper, AuthorizationHelper authorizationHelper) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.authorizationHelper = authorizationHelper;
        this.mapper = mapper;
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

    @GetMapping("/{id}/update")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
          user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            User userToUpdate = userService.getById(id);
            UserDto dto = mapper.toDto(userToUpdate);
            model.addAttribute("userId", id);
            model.addAttribute("user", dto);
            return "UserUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "NotFound";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "AccessDenied";
        }
    }

    @PostMapping("/{id}/update")
    public String handleEditUser(@Valid @ModelAttribute("user") UserDto user,
                                 BindingResult bindingResult,
                                 @PathVariable int id,
                                 Model model,
                                 HttpSession session,
                                 @RequestParam(value = "photoUrl", required = false) String photoUrl) {
        User authenticatedUser;
        try {
            authenticatedUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "UserUpdateView";
        }

            if (!user.getPassword().equals(user.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Passwords must match!");
            return "UserUpdateView";
        }

        try {
            User userToUpdate = mapper.fromDto(id, user);
            if (photoUrl != null && !photoUrl.isEmpty()) {
                // Update the photo URL in the UserDto object
                userToUpdate.setPhotoUrl(photoUrl);
            }
            userService.update(userToUpdate, authenticatedUser);
            return "redirect:/";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "AccessDenied";
        } catch (DuplicateEntityException e) {
            if (e.getMessage().contains("email")) {
                bindingResult.rejectValue("email", "email_error", e.getMessage());
            } else {
                bindingResult.rejectValue("username", "username_error", e.getMessage());
            }
            return "UserUpdateView";
        }  catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "NotFound";
        }
    }
}


//TODO see how you save photoUrl in the database}
