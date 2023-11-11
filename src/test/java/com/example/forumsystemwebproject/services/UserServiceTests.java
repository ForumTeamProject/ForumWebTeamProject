package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockRepository;

    @Mock
    AuthorizationHelper authorizationHelper;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void getById_Should_Throw_WhenUserDoesNotExist() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getById(Mockito.anyInt()));
    }

    @Test
    public void getById_Should_ReturnUserWhenMatchFound() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockUser);

        //Act
        User result = service.getById(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    public void getById_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(null);

        //Act
        service.getById(Mockito.anyInt());

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getById(Mockito.anyInt());
    }

    @Test
    public void get_Should_CallRepository() {
        //Arrange
        UserFilterOptions filterOptions = Helpers.createMockUserFilterOptions();
        Mockito.when(mockRepository.get(filterOptions)).thenReturn(null);

        //Act
        service.get(filterOptions);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).get(filterOptions);
    }

    @Test
    public void getByUsername_Should_Throw_WhenNoMatchFound() {
        //Arrange
        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByUsername(Mockito.anyString()));
    }

    @Test
    public void getByUsername_Should_ReturnWhenMatchFound() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenReturn(mockUser);

        //Act
        User result = service.getByUsername(Mockito.anyString());

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    public void getByUsername_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenReturn(null);

        //Act
        service.getByUsername(Mockito.anyString());

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByUsername(Mockito.anyString());
    }

    @Test
    public void getByEmail_Should_Throw_WhenNoMatchFound() {
        //Arrange
        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByEmail(Mockito.anyString()));
    }

    @Test
    public void getByEmail_Should_ReturnUserWhenMatchFound() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenReturn(mockUser);

        //Act
        User result = service.getByEmail(Mockito.anyString());

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    public void getByEmail_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenReturn(null);

        //Act
        service.getByEmail(Mockito.anyString());

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByEmail(Mockito.anyString());
    }

    @Test
    public void create_Should_Throw_WhenUsernameIsTaken() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenReturn(mockUser);

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(mockUser));
    }

    @Test
    public void create_Should_Throw_WhenEmailIsTaken() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        User mockUser2 = Helpers.createMockUser();
        mockUser2.setId(2);
        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenReturn(mockUser2);

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(mockUser));
    }

    @Test
    public void create_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        //Act
        service.create(mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).create(mockUser);
    }

    @Test
    public void update_Should_Throw_WhenUserIsNotCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        User mockUser2 = Helpers.createMockUser();
        mockUser2.setId(2);

        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(User.class));

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.update(mockUser, mockUser2));
    }

    @Test
    public void update_Should_Throw_WhenEmailIsTaken() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        User mockUser2 = Helpers.createMockUser();
        mockUser2.setId(2);

        Mockito.doNothing().when(authorizationHelper).creatorCheck(Mockito.any(User.class),Mockito.any(User.class));

        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenReturn(mockUser);

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(mockUser2, mockUser));
    }

    @Test
    public void update_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        User mockUser2 = Helpers.createMockUser();

        Mockito.doNothing().when(authorizationHelper).creatorCheck(Mockito.any(User.class),Mockito.any(User.class));

        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        //Act
        service.update(mockUser,  mockUser2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void delete_Should_Throw_WhenUserIsNotCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        User mockDeletedUser = Helpers.createMockUser();
        mockDeletedUser.setId(5);
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockUser);
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockDeletedUser);
        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(User.class));
        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(mockUser , 42));
    }

    @Test
    public void delete_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();

        User mockUserToDelete = Helpers.createMockUser();

        User mockDeletedUser = Helpers.createMockUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockUserToDelete);

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockDeletedUser);

        Mockito.doNothing().when(authorizationHelper).creatorCheck(Mockito.any(User.class),Mockito.any(User.class));

        //Act
        service.delete(mockUser, mockUser.getId());

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockUser, mockUserToDelete);
    }
}
