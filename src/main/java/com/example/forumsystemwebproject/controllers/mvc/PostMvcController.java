package com.example.forumsystemwebproject.controllers.mvc;

import com.example.forumsystemwebproject.exceptions.AuthenticationFailureException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.PaginationHelper;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.PostMapper;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.DTOs.PostDto;
import com.example.forumsystemwebproject.models.DTOs.PostFilterDto;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.CommentService;
import com.example.forumsystemwebproject.services.contracts.LikeService;
import com.example.forumsystemwebproject.services.contracts.PostService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;

    private final AuthenticationHelper authenticationHelper;

    private final PostMapper mapper;

    private final RoleRepository roleRepository;

    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public PostMvcController(PostService postService,
                             AuthenticationHelper authenticationHelper,
                             AuthenticationHelper authenticationHelper1, PostMapper mapper,
                             RoleRepository roleRepository,
                             CommentService commentService, LikeService likeService) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper1;
        this.mapper = mapper;
        this.roleRepository = roleRepository;

        this.commentService = commentService;
        this.likeService = likeService;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            User user = (User) session.getAttribute("currentUser");
            for (Role role : user.getRoles()) {
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


    @ModelAttribute("isBlocked")
    public boolean isBlocked(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            User user = (User) session.getAttribute("currentUser");
            for (Role role : user.getRoles()) {
                if (role.getName().equals(roleRepository.getByName("blockedUser").getName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @GetMapping
    public String showPosts(HttpSession session,
                            Model model,
                            @ModelAttribute("filterOptions") PostFilterDto filterDto,
                            @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        PostFilterOptions filterOptions = new PostFilterOptions(
                filterDto.getUser(),
                filterDto.getTitle(),
                filterDto.getSortBy(),
                filterDto.getSortOrder()

        );
        List<Post> posts = postService.get(filterOptions);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Post> postPage = PaginationHelper.findPaginated(PageRequest.of(currentPage - 1, pageSize), posts);

        model.addAttribute("postPage", postPage);

        int totalPages = postPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("filterOptions", filterDto);
        return "PostsView";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(id);
            List<Comment> comments = commentService.getByPostId(post.getId());

            model.addAttribute("postId", id);
            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            model.addAttribute("tags", post.getTags());
            int likesCount = likeService.getByPostId(id).size();
            model.addAttribute("likesCount", likesCount);

            List<String> tagContents = post.getTags().stream()
                    .map(com.example.forumsystemwebproject.models.Tag::getContent)
                    .collect(Collectors.toList());
            String joinedTags = String.join(",", tagContents);
            model.addAttribute("joinedTags", joinedTags);

            return "PostView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("notFound", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/create")
    public String showCreatePostPage(HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("post", new PostDto());
        return "PostFormView";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PostDto dto, BindingResult bindingResult, HttpSession session, Model model,
                             @RequestParam(value = "photoUrl", required = false) String photoUrl) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "PostFormView";
        }
        try {
            Post post = mapper.fromDto(dto);
            if (photoUrl != null && !photoUrl.isEmpty()) {
                // Update the photo URL in the UserDto object
                post.setPhotoUrl(photoUrl);
            }
            postService.create(post, user);
            model.addAttribute("post", post);
            return "PostView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("unauthorized", e.getMessage());
            return "ErrorView";
        }
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
            return "PostUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("notFound", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@Valid @ModelAttribute("post") PostDto dto, HttpSession session, BindingResult bindingResult, Model model, @PathVariable int id) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "PostUpdateView";
        }

        try {
            Post post = mapper.fromDto(dto);
            postService.update(post, user);
            return "PostsView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("unauthorized", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("notFound", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("unauthorized", e.getMessage());
            return "ErrorView";
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
            int likesCount = likeService.getByPostId(id).size();
            model.addAttribute("likesCount", likesCount);
            //model.addAttribute("likes", postService.getLikes(id));
            return "redirect:/posts/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("notFound", e.getMessage());
            return "ErrorView";
        }
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }


}
