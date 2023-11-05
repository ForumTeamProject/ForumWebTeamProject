package com.example.forumsystemwebproject.controllers.mvc;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final RoleRepository roleRepository;

    private final UserService userService;

    @Autowired
    public UserMvcController(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
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
        if (populateIsAuthenticated(session)) {
            if (isAdmin(session)) {
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
            } else {
                return "AccessDeniedView";
            }
        } else {
            return "redirect:/auth/login";
        }
    }
}
