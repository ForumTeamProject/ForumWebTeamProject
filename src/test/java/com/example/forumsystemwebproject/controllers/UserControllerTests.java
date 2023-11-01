package com.example.forumsystemwebproject.controllers;


import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.helpers.mappers.UserMapper;
import com.example.forumsystemwebproject.models.DTOs.UserDto;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import com.example.forumsystemwebproject.services.contracts.UserService;
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
public class UserControllerTests {

    @MockBean
    UserService mockService;

    @MockBean
    UserMapper mockUserMapper;

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
        List<User> users = new ArrayList<>();
        User expectedUser = Helpers.createMockUser();
        users.add(expectedUser);

        Mockito.when(mockService.get(Mockito.any(UserFilterOptions.class))).thenReturn(users);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedUser.getId()));
    }

    @Test
    public void get_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getById_Should_ReturnStatusOk_WhenUserExists() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockService.getById(Mockito.anyInt())).thenReturn(mockUser);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockUser.getId()));
    }

    @Test
    public void getById_Should_ReturnStatusNotFound_WhenUserDoesNotExist() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockService.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getById_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {

        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void create_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockUserMapper.fromDto(Mockito.any(UserDto.class))).thenReturn(mockUser);
        String body = Helpers.toJson(Helpers.createMockUserDto());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void create_Should_ReturnStatusBadRequest_WhenInvalidRequest() throws Exception {

        //Arrange
        UserDto dto = Helpers.createMockUserDto();
        dto.setPassword("2");

        String body = Helpers.toJson(dto);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void create_Should_ReturnStatusConflict_WhenDuplicateUser() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockUserMapper.fromDto(Mockito.any(UserDto.class))).thenReturn(mockUser);

        Mockito.doThrow(DuplicateEntityException.class).when(mockService).create(mockUser);

        String body = Helpers.toJson(Helpers.createMockUserDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void update_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {

        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        String body = Helpers.toJson(Helpers.createMockUserDto());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void update_Should_ReturnStatusBadRequest_WhenInvalidRequest() throws Exception {

        //Arrange
        UserDto dto = Helpers.createMockUserDto();
        dto.setPassword("2");

        String body = Helpers.toJson(dto);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_Should_ReturnStatusNotFound_WhenUserNotFound() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockUserMapper.fromDto(Mockito.anyInt() ,Mockito.any(UserDto.class))).thenThrow(EntityNotFoundException.class);

        String body = Helpers.toJson(Helpers.createMockUserDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void update_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockUserMapper.fromDto(Mockito.anyInt(), Mockito.any(UserDto.class))).thenReturn(mockUser);

        String body = Helpers.toJson(Helpers.createMockUserDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {

        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void delete_Should_ReturnStatusNotFound_WhenUserDoesNotExist() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class).when(mockService).delete(mockUser, 1);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}