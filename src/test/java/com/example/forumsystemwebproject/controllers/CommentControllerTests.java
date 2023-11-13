package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.CommentMapper;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.DTOs.CommentDto;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTests {

    @MockBean
    CommentService mockService;

    @MockBean
    CommentMapper mockCommentMapper;

    @MockBean
    AuthenticationHelper mockAuthenticationHelper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void get_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {

        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void get_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {

        //Arrange
        List<Comment> comments = new ArrayList<>();
        Comment mockComment = Helpers.createMockComment();
        comments.add(mockComment);
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockService.get(Mockito.any(CommentFilterOptions.class))).thenReturn(comments);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockComment.getId()));
    }

    @Test
    public void getByUserId_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}/comments", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getByUserId_Should_ReturnStatusNotFound_WhenUserNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class).when(mockService).getByUserId(Mockito.anyInt());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}/comments", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getByUserId_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        List<Comment> comments = new ArrayList<>();
        Comment mockComment = Helpers.createMockComment();
        comments.add(mockComment);
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockService.getByUserId(Mockito.anyInt())).thenReturn(comments);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}/comments", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockComment.getId()));
    }

    @Test
    public void getByPostId_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}/comments", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getByPostId_Should_ReturnStatusNotFound_WhenPostNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockService.getByPostId(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}/comments", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getByPostId_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        List<Comment> comments = new ArrayList<>();
        Comment mockComment = Helpers.createMockComment();
        comments.add(mockComment);
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockService.getByPostId(Mockito.anyInt())).thenReturn(comments);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}/comments", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockComment.getId()));
    }

    @Test
    public void getById_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getById_Should_ReturnStatusNotFound_WhenCommentNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockService.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getById_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockService.getById(Mockito.anyInt())).thenReturn(mockComment);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockComment.getId()));
    }

    @Test
    public void create_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);
        String body = Helpers.toJson(Helpers.createMockCommentDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void create_Should_ReturnBadRequest_WhenInvalidRequest() throws Exception {
        //Arrange
        CommentDto dto = Helpers.createMockCommentDto();
        dto.setContent(null);
        String body = Helpers.toJson(dto);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void create_Should_ReturnNotFound_WhenPostNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockCommentMapper.fromDto(Mockito.any(CommentDto.class))).thenReturn(mockComment);
        Mockito.doThrow(EntityNotFoundException.class).when(mockService).create(mockComment, 1, mockUser);
        String body = Helpers.toJson(Helpers.createMockCommentDto());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void create_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockCommentMapper.fromDto(Mockito.any(CommentDto.class))).thenReturn(mockComment);

        String body = Helpers.toJson(Helpers.createMockCommentDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        String body = Helpers.toJson(Helpers.createMockCommentDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/comments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void update_Should_ReturnStatusBadRequest_WhenInvalidRequest() throws Exception {
        //Arrange
        CommentDto dto = Helpers.createMockCommentDto();
        dto.setContent(null);

        String body = Helpers.toJson(dto);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/comments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_Should_ReturnStatusNotFound_WhenCommentNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockCommentMapper.fromDto(Mockito.anyInt(), Mockito.any(CommentDto.class))).thenThrow(EntityNotFoundException.class);

        String body = Helpers.toJson(Helpers.createMockCommentDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/comments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void update_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockCommentMapper.fromDto(Mockito.anyInt(), Mockito.any(CommentDto.class))).thenReturn(mockComment);

        String body = Helpers.toJson(Helpers.createMockCommentDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/comments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void delete_Should_ReturnStatusNotFound_WhenCommentNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class).when(mockService).delete(1, mockUser);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
