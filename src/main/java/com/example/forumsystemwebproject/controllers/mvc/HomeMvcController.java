package com.example.forumsystemwebproject.controllers.mvc;

import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public HomeMvcController(PostRepository postRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String showHomePage() {
        return "home";
    }

    @ModelAttribute("recentlyCreatedPosts")
    public List<Post> mostRecentlyCreatedPosts() {
        return postRepository.getMostRecentlyCreatedPosts();
    }

    @ModelAttribute("mostCommentedPosts")
    public List<Post> mostCommentedPosts() {
        return postRepository.getMostCommented();
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("usersCount")
    public long userCount() {
        return userRepository.getCount();
    }

    @ModelAttribute("posts")
    public long postCount() {
        return postRepository.getCount();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            User user = (User) session.getAttribute("currentUser");
            return user.getRoles().contains(roleRepository.getByName("admin"));
        }
        return false;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
