package com.example.forumsystemwebproject.controllers.mvc;

import com.example.forumsystemwebproject.exceptions.AuthenticationFailureException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.PaginationHelper;
import com.example.forumsystemwebproject.helpers.mappers.CommentMapper;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.DTOs.CommentDto;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.CommentService;
import com.example.forumsystemwebproject.services.contracts.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
@RequestMapping("/posts/{postId}/comments")
public class CommentMvcController {

    private final AuthenticationHelper authenticationHelper;

    private final CommentMapper mapper;

    private final PostService postService;

    private final RoleRepository roleRepository;

    private final CommentService commentService;

    public CommentMvcController(AuthenticationHelper authenticationHelper, RoleRepository roleRepository, CommentService commentService, PostService postService, CommentMapper mapper) {
        this.authenticationHelper = authenticationHelper;
        this.commentService = commentService;
        this.postService = postService;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
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
    public String showComments(@PathVariable int postId, HttpSession session, Model model, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        User user;

        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(postId);
            List<Comment> comments = commentService.getByPostId(postId);

            int currentPage = page.orElse(1);
            int pageSize = size.orElse(10);
            Page<Comment> commentPage = PaginationHelper.findPaginated(PageRequest.of(currentPage - 1, pageSize), comments);
            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            model.addAttribute("commentPage", commentPage);
            int totalPages = commentPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            return "CommentsView";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("{commentId}")
    public String showSingleComment(@PathVariable int postId, @PathVariable int commentId, Model model, HttpSession session) {
        User user;

        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        Comment comment = commentService.getById(commentId);
        Post post = postService.getById(postId);
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        return "CommentView";
    }

//    @GetMapping("/create")
//    public String showCreateCommentPage(@PathVariable int postId, HttpSession session, Model model) {
//        User user;
//
//        try {
//            authenticationHelper.tryGetUser(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/login";
//        }
//
//        model.addAttribute("comment", new CommentDto());
//        model.addAttribute("id", postId);
//        return "CommentFormView";
//    }

    @PostMapping("/create")
    public String handleCreateComment(@PathVariable int postId, @Valid @ModelAttribute("comment") CommentDto dto, BindingResult bindingResult, HttpSession session, Model model) {
        User user;

        try {
           user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + String.valueOf(postId);
           // return "CommentFormView";
        }

        try {
            Comment comment = mapper.fromDto(dto);
            commentService.create(comment, postId, user);
            model.addAttribute("comment", comment);
            return "redirect:/posts/" + postId;// + "/comments";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }



    @GetMapping("/{commentId}/update")
    public String showUpdateCommentPage(@PathVariable int postId, @PathVariable int commentId, HttpSession session, Model model) {
        User user;

        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Comment comment = commentService.getById(commentId);
            CommentDto dto = mapper.toDto(comment);
            model.addAttribute("postId", postId);
            model.addAttribute("commendId", commentId);
            model.addAttribute("comment", dto);
            return "CommentUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @PostMapping("/{commentId}/update")
    public String handleUpdate(@Valid @ModelAttribute("comment") CommentDto dto, BindingResult bindingResult,
                               Model model, HttpSession session, @PathVariable int commentId, @PathVariable int postId) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "CommentUpdateView";
        }

        try {
            Comment comment = mapper.fromDto(commentId, dto);
            commentService.update(comment, user);
            model.addAttribute("postId", postId);
            return "redirect:/posts/" + postId + "/comments";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @PostMapping("{commentId}/delete")
    public String deleteComment(HttpSession session, Model model, @PathVariable int commentId, @PathVariable int postId) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(postId);
            commentService.delete(commentId, user);
            model.addAttribute("post", post);
            return "redirect:/posts/" + postId + "/comments";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


}
