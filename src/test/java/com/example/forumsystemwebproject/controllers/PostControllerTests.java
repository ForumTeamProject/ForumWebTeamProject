package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.PostMapper;
import com.example.forumsystemwebproject.models.DTOs.PostDto;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import com.example.forumsystemwebproject.services.contracts.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTests {

    @MockBean
    PostService mockService;

    @MockBean
    PostMapper mockPostMapper;

    @MockBean
    AuthenticationHelper mockAuthenticationHelper;

    @MockBean
    UserRepository mockUserRepository;

    @MockBean
    PostRepository mockPostRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void get_Should_ReturnStatusOk() throws Exception {
        //Arrange
        List<Post> posts = new ArrayList<>();
        Post expectedPost = Helpers.createMockPost();
        posts.add(expectedPost);

        Mockito.when(mockService.get(Mockito.any(PostFilterOptions.class))).thenReturn(posts);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedPost.getId()));
    }

    @Test
    public void get_Should_Return_StatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getByUserId_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}/posts", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getByUserId_Should_ReturnStatusNotFound_WhenNoUserWithSuchIdFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenReturn(mockUser);

        Mockito.when(mockService.getByUserId(Mockito.any(PostFilterOptions.class), Mockito.anyInt())).thenThrow(EntityNotFoundException.class);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}/posts", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void getByUserId_Should_ReturnStatusOk_WhenUserExists() throws Exception {

        //Arrange
        List<Post> posts = new ArrayList<>();
        Post expectedPost = Helpers.createMockPost();
        posts.add(expectedPost);
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockService.getByUserId(Mockito.any(PostFilterOptions.class), Mockito.anyInt()))
                .thenReturn(posts);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}/posts", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedPost.getId()));
    }

    @Test
    public void getById_Should_ReturnStatusOk_WhenPostExists() throws Exception {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        Mockito.when(mockService.getById(Mockito.anyInt())).thenReturn(mockPost);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockPost.getId()));
    }

    @Test
    public void getById_Should_ReturnStatusNotFound_WhenPostDoesNotExist() throws Exception {
        //Arrange
        Mockito.when(mockService.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void create_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        //Act & Assert
        String body = Helpers.toJson(Helpers.createMockPostDto());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void create_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenReturn(mockUser);

        Post mockPost = Helpers.createMockPost();

        Mockito.when(mockPostMapper.fromDto(Mockito.any())).thenReturn(mockPost);
        String body = Helpers.toJson(Helpers.createMockPostDto());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void create_Should_ReturnStatusBadRequest_WhenBodyIsInvalid() throws Exception {
        //Arrange
        PostDto dto = Helpers.createMockPostDto();
        dto.setTitle(null);


        //Act, Assert
        String body = Helpers.toJson(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        //Act & Assert
        String body = Helpers.toJson(Helpers.createMockPostDto());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void update_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenReturn(mockUser);

        Post mockPost = Helpers.createMockPost();

        Mockito.when(mockPostMapper.fromDto(Mockito.any())).thenReturn(mockPost);

        //Act & Assert
        String body = Helpers.toJson(Helpers.createMockPostDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_Should_ReturnStatusBadRequest_WhenBodyIsInvalid() throws Exception {
        //Arrange
        PostDto dto = Helpers.createMockPostDto();
        dto.setTitle(null);


        //Act, Assert
        String body = Helpers.toJson(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void delete_Should_ReturnStatusNotFound_WhenNoPostMatchFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class)))
                .thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class).when(mockService).delete(3, mockUser);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void likePost_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void likePost_Should_ReturnStatusNotFound_WhenPostNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class).when(mockService).likePost(Mockito.anyInt(), Mockito.any(User.class));
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void likePost_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
 }
