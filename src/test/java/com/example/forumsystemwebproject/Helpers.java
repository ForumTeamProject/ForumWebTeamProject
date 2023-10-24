package com.example.forumsystemwebproject;

import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.CommentMapper;
import com.example.forumsystemwebproject.models.*;
import com.example.forumsystemwebproject.models.DTOs.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class Helpers {

    /**
     * Accepts an object and returns the stringified object.
     * Useful when you need to pass a body to a HTTP request.
     */
    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Comment createMockComment() {
        var mockComment = new Comment();
        mockComment.setPost(createMockPost());
        mockComment.setId(1);
        mockComment.setContent("This is a mock content for mock comment.");
        mockComment.setUser(createMockUser());
        return mockComment;
    }

    public static PhoneNumber createMockPhoneNumber() {
        var mockPhoneNumber = new PhoneNumber();
        mockPhoneNumber.setId(1);
        mockPhoneNumber.setNumber("0889765849");
        mockPhoneNumber.setUser(createMockUser());
        return mockPhoneNumber;
    }

    public static Post createMockPost()  {
        var mockPost = new Post();
        Set<Like> likes = new HashSet<>();
        Set<Tag> tags = new HashSet<>();
        mockPost.setContent("This is a mock content for post creation.");
        mockPost.setTitle("Mock title for mock post.");
        mockPost.setId(1);
        mockPost.setUser(createMockUser());
        mockPost.setCreationDate(new Date());
        mockPost.setLikes(likes);
        mockPost.setTags(tags);
        return mockPost;
    }

    public static List<Post> createMockPostList() {
        var mockPostList = new ArrayList<Post>();
        mockPostList.add(createMockPost());
        return mockPostList;
    }

    public static List<Comment> createMockCommentList() {
        var mockCommentList = new ArrayList<Comment>();
        mockCommentList.add(createMockComment());
        return mockCommentList;
    }

    public static Role createMockUserRole() {
        var mockRole = new Role();
        mockRole.setId(1);
        mockRole.setName("user");
        return mockRole;
    }

    public static Role createMockAdminRole() {
        var mockRole = new Role();
        mockRole.setId(2);
        mockRole.setName("admin");
        return mockRole;
    }

    public static Set<Role> createUserMockRoles() {
        var mockRoles = new HashSet<Role>();
        mockRoles.add(createMockUserRole());
        return mockRoles;
    }


    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setRoles(createUserMockRoles());
        mockUser.setEmail("mockmail@gmail.com");
        mockUser.setPassword("Aa123456@");
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        return mockUser;
    }

    public static Like createMockLike() {
        var mockLike = new Like();
        mockLike.setId(1);
        mockLike.setPost(createMockPost());
        mockLike.setUser(createMockUser());
        return mockLike;
    }

    public static Tag createMockTag() {
        var mockTag = new Tag();
        mockTag.setId(1);
        mockTag.setContent("This is a mock tag.");
        return mockTag;
    }

    public static CommentDto createMockCommentDto() {
        var mockCommentDto = new CommentDto();
        mockCommentDto.setContent("This is a content for mock comment dto");
        return mockCommentDto;
    }
    
    public static PhoneNumberDto createMockPhoneNumberDto() {
        var mockPhoneNumberDto = new PhoneNumberDto();
        mockPhoneNumberDto.setNumber("0899878786");
        return mockPhoneNumberDto;
    }
    
    public static PostDto createMockPostDto() {
        PostDto mockPostDto = new PostDto();
        mockPostDto.setContent("This is a mock content for mock Post Dto.");
        mockPostDto.setTitle("This is a mock title for mock Post Dto");
        return mockPostDto;
    }

    public static RoleDto createMockRoleDto() {
        var mockRoleDto = new RoleDto();
        mockRoleDto.setName("mockRoleName");
        return mockRoleDto;
    }

    public static UserDto createMockUserDto() {
        var mockUserDto = new UserDto();
        mockUserDto.setFirstName("MockUserFirstName");
        mockUserDto.setLastName("MockUserLastName");
        mockUserDto.setEmail("mockemail@gmail.com");
        mockUserDto.setUsername("mockUsername");
        mockUserDto.setPassword("Aa123456@");
        return mockUserDto;
    }

    public static TagDto createMockTagDto() {
        var mockTagDto = new TagDto();
        mockTagDto.setContent("This is a mock tag content");
        return mockTagDto;
    }

    public static PostFilterOptions createMockPostFilterOptions() {
        return new PostFilterOptions(
            "user",
                "mockTitle",
                "sort",
                "order"
        );
    }

    public static CommentFilterOptions createMockCommentFilterOptions() {
        return new CommentFilterOptions(
                "user",
                "mockContent",
                "sort",
                "order"
        );
    }

    public static UserFilterOptions createMockUserFilterOptions() {
        return new UserFilterOptions(
                "mockUsername",
                "mockMail@gmail.com",
                "mockFirstName",
                "mockLastName",
                "sort",
                "order"
        );
    }
}
