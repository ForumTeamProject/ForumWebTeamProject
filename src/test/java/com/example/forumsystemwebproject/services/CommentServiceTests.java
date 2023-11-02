package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.CommentFilterOptions;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.CommentRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
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
    public void getByUserId_Should_CallRepository() {

        //Arrange
        CommentFilterOptions filterOptions = Helpers.createMockCommentFilterOptions();
        Mockito.when(mockRepository.getByUserId(Mockito.eq(filterOptions), Mockito.anyInt()))
                .thenReturn(null);

        //Act
        service.getByUserId(filterOptions, 2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByUserId(Mockito.eq(filterOptions), Mockito.anyInt());
    }

    @Test
    public void getByUserId_Should_ReturnCommentList_WhenUserExists() {
        //Arrange
        List<Comment> mockCommentList = Helpers.createMockCommentList();
        CommentFilterOptions filterOptions = Helpers.createMockCommentFilterOptions();
        Mockito.when(mockRepository.getByUserId(Mockito.eq(filterOptions), Mockito.anyInt()))
                .thenReturn(mockCommentList);

        //Act
        List<Comment> result = service.getByUserId(filterOptions, 2);

        //Assert
        Assertions.assertEquals(mockCommentList, result);
    }

    @Test
    public void getByUserId_Should_Throw_WhenUserDoesNotExist() {
        //Arrange
        CommentFilterOptions filterOptions = Helpers.createMockCommentFilterOptions();
        Mockito.when(mockRepository.getByUserId(Mockito.eq(filterOptions), Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException("User", Mockito.anyInt()));

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByUserId(filterOptions, Mockito.anyInt()));

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
        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);

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

        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);
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
        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(true);

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.create(mockComment, 1, mockUser));
    }

    @Test
    public void create_Should_SetPost_WhenInvoked() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        Post mockPost = Helpers.createMockPost();

        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);

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

        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);

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

        Mockito.when(authorizationHelper.isCreator(Mockito.any(User.class), Mockito.any(Comment.class))).thenReturn(true);
        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);
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
        User mockUser = Helpers.createMockUser();
        mockUser.setId(2);
        Comment mockComment = Helpers.createMockComment();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockComment);

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(Mockito.anyInt(), mockUser));
    }
    @Test
    public void delete_Should_CallRepository() {
        //Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockUser();

        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);
        Mockito.when(authorizationHelper.isCreator(Mockito.any(User.class), Mockito.any(Comment.class))).thenReturn(true);
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockComment);

        //Act
        service.delete(2, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockComment);
    }

}

