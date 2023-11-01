package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.LikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTests {

    @Mock
    LikeRepository mockRepository;

    @InjectMocks
    LikeServiceImpl service;

    @Test
    public void get_Should_CallRepository() {

        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();

        //Act
        service.get(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).get(mockPost, mockUser);
    }

    @Test
    public void  get_Should_ReturnLike_WhenExists() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        Like mockLike = Helpers.createMockLike();
        Mockito.when(mockRepository.get(mockPost, mockUser)).thenReturn(mockLike);

        //Act
        Like result = service.get(mockPost, mockUser);

        //Assert
        Assertions.assertEquals(mockLike,result);
    }

    @Test
    public void get_Should_Throw_WhenNoMatch() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockRepository.get(mockPost,mockUser)).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.get(mockPost, mockUser));
    }

    @Test
    public void getByUserId_Should_CallRepository() {

        //Arrange
        Mockito.when(mockRepository.getByUserId(Mockito.anyInt())).thenReturn(null);

        //Act
        service.getByUserId(2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByUserId(Mockito.anyInt());

    }

    @Test
    public void getByUserId_Should_ReturnList_WhenMatchFound() {
        //Arrange
        List<Like> likes = new ArrayList<>();
        Like mockLike = Helpers.createMockLike();
        likes.add(mockLike);

        Mockito.when(mockRepository.getByUserId(Mockito.anyInt())).thenReturn(likes);

        //Act
        List<Like> result = service.getByUserId(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(likes, result);
    }

    @Test
    public void getByUserId_Should_Throw_WhenNoUserExists() {
        //Arrange
        Mockito.when(mockRepository.getByUserId(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByUserId(Mockito.anyInt()));
    }

    @Test
    public void getByPostId_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getByPostId(Mockito.anyInt())).thenReturn(null);

        //Act
        service.getByPostId(2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByPostId(Mockito.anyInt());
    }

    @Test
    public void getByPostId_Should_ReturnListWhenMatchFound() {
        //Arrange
        List<Like> likes = new ArrayList<>();
        Like mockLike = Helpers.createMockLike();
        likes.add(mockLike);

        Mockito.when(mockRepository.getByPostId(Mockito.anyInt())).thenReturn(likes);

        //Act
        List<Like> result = service.getByPostId(2);

        //Assert
        Assertions.assertEquals(likes, result);
    }

    @Test
    public void getByPostId_Should_Throw_WhenNoPostFound() {
        //Arrange
        Mockito.when(mockRepository.getByPostId(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByPostId(Mockito.anyInt()));
    }

    @Test
    public void getById_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(null);

        //Act
        service.getById(2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getById(Mockito.anyInt());
    }

    @Test
    public void getById_Should_ReturnLike_WhenMatchFound() {
        //Arrange
        Like mockLike = Helpers.createMockLike();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockLike);

        //Act
        Like result = service.getById(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(mockLike, result);
    }

    @Test
    public void getById_Should_Throw_WhenNoMatchFound() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getById(Mockito.anyInt()));
    }

    @Test
    public void create_Should_CallRepository() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();

        Mockito.doNothing().when(mockRepository).create(Mockito.any(Like.class));

        //Act
        service.create(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).create(Mockito.any(Like.class));
    }

    @Test
    public void update_Should_CallRepository() {
        //Arrange
        Like mockLike = Helpers.createMockLike();
        User mockUser = Helpers.createMockUser();

        Mockito.doNothing().when(mockRepository).update(mockLike);

        //Act
        service.update(mockLike, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockLike);
    }

    @Test
    public void delete_Should_CallRepository() {
        //Arrange
        Like mockLike = Helpers.createMockLike();
        Mockito.doNothing().when(mockRepository).delete(mockLike);

        //Act
        service.delete(mockLike);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockLike);
    }
}
