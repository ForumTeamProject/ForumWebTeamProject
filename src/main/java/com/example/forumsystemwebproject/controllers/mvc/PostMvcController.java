package com.example.forumsystemwebproject.controllers.mvc;

import com.example.forumsystemwebproject.exceptions.AuthenticationFailureException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.PostMapper;
import com.example.forumsystemwebproject.models.DTOs.PostDto;
import com.example.forumsystemwebproject.models.DTOs.PostFilterDto;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;

    private final AuthenticationHelper authenticationHelper;

    private final PostMapper mapper;

    private final RoleRepository roleRepository;

    @Autowired
    public PostMvcController(PostService postService,
                             AuthenticationHelper authenticationHelper,
                             PostMapper mapper,
                             RoleRepository roleRepository) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") == null;
    }

    @GetMapping
    public String showPosts(HttpSession session, Model model, @Valid @ModelAttribute("filters") PostFilterDto dto) {
        if (populateIsAuthenticated(session)) {
            return "redirect:/auth/login";
        }
        model.addAttribute("posts", postService.get(new PostFilterOptions(
                dto.getUser(),
                dto.getTitle(),
                dto.getSortBy(),
                dto.getSortOrder())));
        model.addAttribute("filters", dto);
        return "posts";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model, HttpSession session) {
        if (populateIsAuthenticated(session)) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(id);
            model.addAttribute("post", post);
            return "post";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/create")
    public String showCreatePostPage(HttpSession session, Model model) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect/auth/login";
        }
        model.addAttribute("post", new PostDto());
        return "PostForm";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PostDto dto, HttpSession session, BindingResult bindingResult) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "post-new";
        }

        Post post = mapper.fromDto(dto);
        postService.create(post, user);
        return "posts";
    }

    @GetMapping("/{id}/update")
    public String showUpdatePostPage(@PathVariable int id, HttpSession session, Model model) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(id);
            PostDto dto = mapper.toDto(post);
            model.addAttribute("postId", id);
            model.addAttribute("post", dto);
            return "post-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@Valid @ModelAttribute("post") PostDto dto, HttpSession session, BindingResult bindingResult, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "post-update";
        }

        try {
            Post post = mapper.fromDto(dto);
            postService.update(post, user);
            return "posts";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect/auth/login";
        }

        try {
            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/like")
    public String likePost(@PathVariable int id, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            postService.likePost(id, user);
            Post updatedPost = postService.getById(id);
            model.addAttribute("post", updatedPost);
            return "post";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user != null && user.getRoles() != null) {
            return user.getRoles().contains(roleRepository.getByName("admin"));
        }

        return false;
    }
    //TODO this method needs to be reworked probably after the apply theme lecture ^^^
}
