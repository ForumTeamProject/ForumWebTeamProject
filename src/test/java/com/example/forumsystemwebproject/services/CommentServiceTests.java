package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.CommentRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @Mock
    CommentRepository mockRepository;

    @Mock
    AuthorizationHelper authorizationHelper;

    @Mock
    PostRepository mockPostRepository;

    @InjectMocks
    CommentServiceImpl service;


    @Test
    public void get_Should_CallRepository() {

        CommentFilterOptions filterOptions = Helpers.createMockCommentFilterOptions();

        //Arrange
        Mockito.when(mockRepository.get(Mockito.any(CommentFilterOptions.class))).thenReturn(null);

        //Act
        service.get(filterOptions);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).get(filterOptions);
    }

    @Test
    public void getById_Should_CallRepository() {

        //Act
        service.getById(Mockito.anyInt());

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getById(Mockito.anyInt());

    }

    @Test
    public void getById_Should_ReturnComment_WhenValid() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockComment);
        //Act
        Comment result = service.getById(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(mockComment, result);
    }

    @Test
    public void getById_Should_Throw_WhenCommentDoesNotExist() {
        //Arrange
        Mockito.doThrow(EntityNotFoundException.class).when(mockRepository).getById(Mockito.anyInt());

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getById(2));
    }


    @Test
    public void getByUserId_Should_CallRepository() {

        //Arrange
        Mockito.when(mockRepository.getByUserId(Mockito.anyInt()))
                .thenReturn(null);

        //Act
        service.getByUserId(2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByUserId(Mockito.anyInt());
    }

    @Test
    public void getByUserId_Should_ReturnCommentList_WhenUserExists() {
        //Arrange
        List<Comment> mockCommentList = Helpers.createMockCommentList();
        Mockito.when(mockRepository.getByUserId(Mockito.anyInt()))
                .thenReturn(mockCommentList);

        //Act
        List<Comment> result = service.getByUserId(2);

        //Assert
        Assertions.assertEquals(mockCommentList, result);
    }

    @Test
    public void getByUserId_Should_Throw_WhenUserDoesNotExist() {
        //Arrange
        Mockito.doThrow(EntityNotFoundException.class).when(mockRepository).getByUserId(Mockito.anyInt());
        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByUserId(Mockito.anyInt()));

    }

    @Test
    public void getByPostId_Should_CallRepository() {
        //Arrange
        Post mockPost = Helpers.createMockPost();

        Mockito.when(mockPostRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        Mockito.when(mockRepository.getByPostId(mockPost)).thenReturn(null);

        //Act
        service.getByPostId(Mockito.anyInt());

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByPostId(mockPost);

    }

    @Test
    public void getByPostId_Should_Throw_WhenPostDoesNotExist() {

        //Arrange
        Mockito.when(mockPostRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByPostId(Mockito.anyInt()));
    }

    @Test
    public void getByPostId_Should_ReturnCommentList_WhenPostExist() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        List<Comment> mockCommentList = Helpers.createMockCommentList();

        Mockito.when(mockPostRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        Mockito.when(mockRepository.getByPostId(mockPost)).thenReturn(mockCommentList);
        //Act
        List<Comment> result = service.getByPostId(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(mockCommentList, result);
    }

    @Test
    public void create_Should_Throw_When_PostDoesNotExist() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockUser();

        Mockito.doNothing().when(authorizationHelper).blockedCheck(Mockito.any(User.class));
        Mockito.when(mockPostRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.create(mockComment,1, mockUser));
    }

    @Test
    public void create_Should_CallRepository() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();

        Mockito.doNothing().when(authorizationHelper).blockedCheck(Mockito.any(User.class));
        Mockito.when(mockPostRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        //Act
        service.create(mockComment, 1, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).create(mockComment);
    }

    @Test
    public void create_Should_Throw_WhenUserBlocked() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockUser();

        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).blockedCheck(Mockito.any(User.class));
        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.create(mockComment, 1, mockUser));
    }

    @Test
    public void create_Should_SetPost_WhenInvoked() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        Post mockPost = Helpers.createMockPost();

        Mockito.doNothing().when(authorizationHelper).blockedCheck(Mockito.any(User.class));

        Mockito.when(mockPostRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        //Act
        service.create(mockComment, 1,  Helpers.createMockUser());

        //Assert
        Assertions.assertEquals(mockComment.getPost(), mockPost);
    }

    @Test
    public void update_Should_Throw_WhenUserIsNotCreator() {

        //Arrange
        User mockUser = Helpers.createMockUser();
        User mockUser2 = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();

        Mockito.doNothing().when(authorizationHelper).blockedCheck(Mockito.any(User.class));
        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Comment.class));

        mockUser2.setId(2);
        mockComment.setUser(mockUser2);

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.update(mockComment, mockUser));
    }

    @Test
    public void update_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();
        Mockito.doNothing().when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Comment.class));
        Mockito.doNothing().when(authorizationHelper).blockedCheck(Mockito.any(User.class));

        //Act
        service.update(mockComment, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockComment);
    }

    @Test
    public void delete_Should_Throw_WhenCommentDoesNotExist() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(Mockito.anyInt(),mockUser));
    }


    @Test
    public void delete_Should_Throw_WhenUserIsNotCreator() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser2 = Helpers.createMockUser();
        mockUser2.setId(5);
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockComment);
        Mockito.doNothing().when(authorizationHelper).blockedCheck(Mockito.any(User.class));
        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Comment.class));
        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(1, mockUser2));
    }
    @Test
    public void delete_Should_CallRepository() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockUser();

        Mockito.doNothing().when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Comment.class));
        Mockito.doNothing().when(authorizationHelper).blockedCheck(Mockito.any(User.class));
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockComment);

        //Act
        service.delete(2, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockComment);
    }

}

