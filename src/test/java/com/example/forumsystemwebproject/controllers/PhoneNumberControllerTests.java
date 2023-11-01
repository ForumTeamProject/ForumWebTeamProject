package com.example.forumsystemwebproject.controllers;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthenticationHelper;
import com.example.forumsystemwebproject.helpers.mappers.PhoneNumberMapper;
import com.example.forumsystemwebproject.models.DTOs.PhoneNumberDto;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.PhoneNumberService;
import jakarta.persistence.Table;
import org.apache.tomcat.util.security.Escape;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
public class PhoneNumberControllerTests {

    @MockBean
    PhoneNumberService mockService;

    @MockBean
    AuthenticationHelper mockAuthenticationHelper;

    @MockBean
    PhoneNumberMapper mockNumberMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void get_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/phone-numbers"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void get_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        List<PhoneNumber> numbers = new ArrayList<>();
        PhoneNumber number = Helpers.createMockPhoneNumber();
        numbers.add(number);
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockService.get()).thenReturn(numbers);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/phone-numbers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(number.getId()));
    }


    @Test
    public void getById_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception{

        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/phone-numbers/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getById_Should_ReturnStatusNotFound_WhenPhoneNumberIdInvalid() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockService.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/phone-numbers/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void getById_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception{
        //Arrange
        User mockUser = Helpers.createMockUser();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockService.getById(Mockito.anyInt())).thenReturn(mockNumber);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/phone-numbers/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockNumber.getId()));
    }

    @Test
    public void create_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/phone-numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void create_Should_ReturnStatusBadRequest_WhenInvalidRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        PhoneNumberDto dto = Helpers.createMockPhoneNumberDto();
        dto.setNumber("0");
        String body = Helpers.toJson(dto);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/phone-numbers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void create_Should_ReturnStatusConflict_WhenDuplicatedNumber() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockNumberMapper.fromDto(Mockito.any(PhoneNumberDto.class))).thenReturn(mockNumber);

        Mockito.doThrow(DuplicateEntityException.class).when(mockService).create(mockNumber, mockUser);
        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/phone-numbers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void create_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {

        //Arrange
        User mockUser = Helpers.createMockUser();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);
        Mockito.when(mockNumberMapper.fromDto(Mockito.any(PhoneNumberDto.class))).thenReturn(mockNumber);

        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/phone-numbers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/phone-numbers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void update_Should_ReturnStatusBadRequest_WhenInvalidRequest() throws Exception {

        //Arrange
        PhoneNumberDto dto = Helpers.createMockPhoneNumberDto();
        dto.setNumber("2");
        String body = Helpers.toJson(dto);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/phone-numbers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.when(mockNumberMapper.fromDto(Mockito.any(PhoneNumberDto.class))).thenReturn(mockNumber);
        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/phone-numbers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_WhenUserNotAuthenticated() throws Exception {
        //Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenThrow(UnauthorizedOperationException.class);

        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/phone-numbers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void delete_Should_ReturnStatusNotFound_WhenNumberNotFound() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class).when(mockService).delete(1, mockUser);

        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/phone-numbers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_Should_ReturnStatusOk_WhenCorrectRequest() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any(HttpHeaders.class))).thenReturn(mockUser);

        String body = Helpers.toJson(Helpers.createMockPhoneNumberDto());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/phone-numbers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
