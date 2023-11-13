package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.*;
import com.example.forumsystemwebproject.repositories.contracts.LikeRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.LikeService;
import com.example.forumsystemwebproject.services.contracts.TagService;
import org.hibernate.id.uuid.Helper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    PostRepository mockRepository;

    @Mock
    AuthorizationHelper authorizationHelper;


    @Mock
    TagService tagService;

    @Mock
    LikeService mockLikeService;

    @InjectMocks
    PostServiceImpl service;

    @Test
    public void get_Should_CallRepository() {
        //Arrange
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        Mockito.when(mockRepository.get(filterOptions))
                .thenReturn(null);

        //Act
        service.get(filterOptions);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).get(filterOptions);
    }


    @Test
    public void getById_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(null);

        //Act
        service.getById(Mockito.anyInt());

        Mockito.verify(mockRepository, Mockito.times(1)).getById(Mockito.anyInt());
    }

    @Test
    public void getById_Should_Throw_WhenNoMatchFound() {
        //Arrange
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getById(2));
    }

    @Test
    public void getById_Should_ReturnPost_When_MatchByIdExists() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockPost);

        //Act
        Post result = service.getById(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(mockPost, result);
    }


    @Test
    public void getByUserId_Should_CallRepository() {
        //Arrange
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        Mockito.when(mockRepository.getByUserId(Mockito.eq(filterOptions), Mockito.anyInt()))
                .thenReturn(null);

        //Act
        service.getByUserId(filterOptions, 2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByUserId(Mockito.eq(filterOptions), Mockito.anyInt());
    }

    @Test
    public void getByUserId_Should_ReturnPostList_WhenUserExists() {
        //Arrange
        List<Post> mockPostList = Helpers.createMockPostList();
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        Mockito.when(mockRepository.getByUserId(Mockito.eq(filterOptions), Mockito.anyInt()))
                .thenReturn(mockPostList);

        //Act
        List<Post> result = service.getByUserId(filterOptions, 2);


        //Assert
        Assertions.assertEquals(mockPostList, result);
    }

    @Test
    public void getByUserId_Should_Throw_WhenUserDoesNotExist() {
        //Arrange
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        Mockito.when(mockRepository.getByUserId(Mockito.any(PostFilterOptions.class), Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByUserId(filterOptions, 2));
    }

    @Test
    public void create_Should_CallRepository() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        Mockito.doNothing().when(mockRepository).create(Mockito.any(Post.class));

        //Act
        service.create(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).create(mockPost);
    }

    @Test
    public void create_Should_SetUserOfPost_WhenInvoked() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        Mockito.doNothing().when(mockRepository).create(Mockito.any(Post.class));

        //Act
        service.create(mockPost, mockUser);

        //Assert
        Assertions.assertEquals(mockPost.getUser(), mockUser);
    }

    @Test
    public void create_Should_Throw_WhenUserBlocked() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        mockUser.getRoles().add(Helpers.createMockBlockRole());

        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).blockedCheck(Mockito.any(User.class));

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.create(mockPost, mockUser));
    }

//    @Test
//    public void update_Should_CallRepository_WhenUserIsAdmin() {
//        //Arrange
//        User mockUser = Helpers.createMockUser();
//        Post mockPost = Helpers.createMockPost();
//        Role mockRole = Helpers.createMockAdminRole();
//
//        Mockito.doNothing().when(mockRepository).update(mockPost);
//
//        Mockito.when(authorizationHelper.isAdmin(Mockito.any(User.class))).thenReturn(true);
//        Mockito.when(authorizationHelper.isBlockedUser(Mockito.any(User.class))).thenReturn(false);
//
//        mockUser.getRoles().add(mockRole);
//
//        //Act
//        service.update(mockPost, mockUser);
//
//        //Assert
//        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
//    }

    @Test
    public void update_Should_CallRepository_WhenUserIsCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();

        Mockito.doNothing().when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Post.class));

        Mockito.doNothing().when(mockRepository).update(mockPost);

        mockPost.setUser(mockUser);

        //Act
        service.update(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }

    @Test
    public void update_Should_Throw_WhenUserIsNotAdminOrCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();

        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Post.class));

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.update(mockPost, mockUser));
    }

    @Test
    public void delete_Should_CallRepository_WhenUserIsAdmin() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser2 = Helpers.createMockUser();
        mockUser2.setId(2);
        mockUser2.getRoles().add(Helpers.createMockAdminRole());

        Mockito.when(service.getById(Mockito.anyInt())).thenReturn(mockPost);

        //Act
        service.delete(1, mockUser2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockPost);
    }

    @Test
    public void delete_Should_CallRepository_WhenUserIsCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();

        mockPost.setUser(mockUser);

        Mockito.when(service.getById(Mockito.anyInt())).thenReturn(mockPost);

        //Act
        service.delete(Mockito.anyInt(), mockUser);


        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockPost);
    }

    @Test
    public void delete_ShouldThrow_WhenUserIsNotCreatorOrAdmin() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();

        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockPost);
        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Post.class));
        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).adminCheck(Mockito.any(User.class));

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(Mockito.anyInt(), mockUser));
    }

    @Test
    public void likePost_Should_Throw_WhenPostDoesNotExist() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.likePost(2, mockUser));
    }

    @Test
    public void likePost_Should_CallLikeServiceToDelete_WhenLikeExist() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        Like mockLike = Helpers.createMockLike();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        Mockito.when(mockLikeService.get(mockPost, mockUser)).thenReturn(mockLike);

        //Act
        service.likePost(2, mockUser);

        //Assert
        Mockito.verify(mockLikeService, Mockito.times(1)).delete(mockLike);

    }

    @Test
    public void likePost_Should_CallLikeServiceToCreate_WhenLikeDoesNotExist() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        Mockito.when(mockLikeService.get(mockPost, mockUser)).thenThrow(EntityNotFoundException.class);

        //Act
        service.likePost(2,mockUser);

        //Assert
        Mockito.verify(mockLikeService, Mockito.times(1)).create(mockPost, mockUser);
    }

    @Test
    public void getMostCommented_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getMostCommented()).thenReturn(null);

        //Act
        service.getMostCommented();

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getMostCommented();
    }

    @Test
    public void getMostRecentlyCreatedPosts_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getMostRecentlyCreatedPosts()).thenReturn(null);

        //Act
        service.getMostRecentlyCreatedPosts();

        //Assert
        Mockito.verify(mockRepository,Mockito.times(1)).getMostRecentlyCreatedPosts();
    }

    @Test
    public void addTagToPost_Should_Throw_WhenUserIsBlocked() {
        //Arrange
        User mockAuthenticatedUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();

        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).blockedCheck(Mockito.any(User.class));

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.addTagToPost(mockAuthenticatedUser, mockPost, mockTag));
    }

    @Test
    public void addTagToPost_Should_Throw_WhenUserIsNotCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).creatorCheck(Mockito.any(User.class), Mockito.any(Post.class));

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.addTagToPost(mockUser, mockPost, mockTag));
    }

    @Test
    public void addTagToPost_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        Mockito.when(tagService.create(Mockito.any(Tag.class), Mockito.any(User.class))).thenReturn(mockTag);

        //Act
        service.addTagToPost(mockUser, mockPost, mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }

    @Test
    public void deleteTagFromPost_Should_Throw_WhenUserIsBlocked() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        Mockito.doThrow(UnauthorizedOperationException.class).when(authorizationHelper).blockedCheck(Mockito.any(User.class));


        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.deleteTagFromPost(mockUser, mockPost, mockTag));
    }

    @Test
    public void deleteTagFromPost_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        //Act
        service.deleteTagFromPost(mockUser, mockPost, mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }
}
