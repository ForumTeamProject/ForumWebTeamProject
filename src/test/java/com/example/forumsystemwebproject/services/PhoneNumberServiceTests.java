package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PhoneNumberRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import org.hibernate.action.internal.EntityActionVetoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.HandlerExceptionResolver;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberServiceTests {

    @Mock
    PhoneNumberRepository mockRepository;

    @Mock
    AuthorizationHelper authorizationHelper;

    @Mock
    RoleRepository mockRoleRepository;

    @InjectMocks
    PhoneNumberServiceImpl service;

    @Test
    public void get_Should_CallRepository() {

        //Arrange
        Mockito.when(mockRepository.get()).thenReturn(null);

        //Act
        service.get();

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).get();
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
    public void getById_Should_Throw_WhenNoMatchFound() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getById(Mockito.anyInt()));
    }

    @Test
    public void getById_Should_ReturnPhoneNumberWhenMatchByIdExists() {
        //Arrange
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockNumber);

        //Act
        PhoneNumber result = service.getById(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(mockNumber, result);
    }

    @Test
    public void create_Should_Throw_WhenUserNotAdmin() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Role mockRole = Helpers.createMockAdminRole();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();

        Mockito.when(authorizationHelper.isAdmin(Mockito.any(User.class))).thenReturn(false);

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.create(mockNumber, mockUser));
    }

    @Test
    public void create_Should_SetUserWhenInvoked() {

        //Arrange
        User mockUser = Helpers.createMockUser();
        Role mockRole = Helpers.createMockAdminRole();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        mockUser.getRoles().add(mockRole);

        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);
        Mockito.when(authorizationHelper.isAdmin(Mockito.any(User.class))).thenReturn(true);

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act
        service.create(mockNumber, mockUser);

        //Assert
        Assertions.assertEquals(mockNumber.getUser(), mockUser);
    }

    @Test
    public void create_Should_Throw_WhenNumberIsAlreadyTaken() {
        //Arrange

        User mockUser = Helpers.createMockUser();
        Role mockRole = Helpers.createMockAdminRole();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        mockUser.getRoles().add(mockRole);

        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);
        Mockito.when(authorizationHelper.isAdmin(Mockito.any(User.class))).thenReturn(true);

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockNumber);

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(mockNumber, mockUser));
    }

    @Test
    public void create_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Role mockRole = Helpers.createMockAdminRole();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        mockUser.getRoles().add(mockRole);

        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);
        Mockito.when(authorizationHelper.isAdmin(Mockito.any(User.class))).thenReturn(true);

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act
        service.create(mockNumber, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).create(mockNumber);
    }

    @Test
    public void update_Should_Throw_WhenUserIsNotCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        mockUser.setId(2);

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.update(mockNumber, mockUser));
    }

    @Test
    public void update_Should_Throw_WhenNumberIsTaken() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();

        Mockito.when(authorizationHelper.isCreator(Mockito.any(User.class), Mockito.any(PhoneNumber.class))).thenReturn(true);

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockNumber);

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(mockNumber, mockUser));
    }

    @Test
    public void update_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();

        Mockito.when(authorizationHelper.isCreator(Mockito.any(User.class), Mockito.any(PhoneNumber.class))).thenReturn(true);
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act
        service.update(mockNumber, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockNumber);
    }

    @Test
    public void delete_Should_Throw_WhenNumberDoesNotExist() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(Mockito.anyInt(), mockUser));
    }

    @Test
    public void delete_Should_Throw_WhenUserIsNotCreator() {
        //Arrange
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        User mockUser = Helpers.createMockUser();
        mockUser.setId(2);
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockNumber);

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(Mockito.anyInt(), mockUser));
    }

    @Test
    public void delete_Should_CallRepository() {
        //Arrange
        PhoneNumber mockNumber = Helpers.createMockPhoneNumber();
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockNumber);

        Mockito.when(authorizationHelper.isCreator(Mockito.any(User.class), Mockito.any(PhoneNumber.class))).thenReturn(true);

        //Act
        service.delete(Mockito.anyInt(), mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockNumber);
    }
}
