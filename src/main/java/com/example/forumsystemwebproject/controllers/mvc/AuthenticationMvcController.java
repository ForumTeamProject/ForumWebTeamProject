package com.example.forumsystemwebproject.controllers.mvc;

import com.example.forumsystemwebproject.exceptions.AuthenticationFailureException;
import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.mappers.UserMapper;
import com.example.forumsystemwebproject.models.DTOs.LoginDto;
import com.example.forumsystemwebproject.models.DTOs.UserDto;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;

    private final UserMapper userMapper;

    private final UserService userService;

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserMapper userMapper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "LoginView";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser", user);
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("password", "password_error", e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new UserDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") UserDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Passwords must match!");
            return "RegisterView";
        }

        try {
            User user = userMapper.fromDto(dto);
            userService.create(user);
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            if (e.getMessage().contains("email")) {
                bindingResult.rejectValue("email", "email_error", e.getMessage());
            } else {
                bindingResult.rejectValue("username", "username_error", e.getMessage());
            }
            return "RegisterView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }
}
