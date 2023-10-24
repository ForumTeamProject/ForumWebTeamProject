package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.LikeRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.services.contracts.LikeService;
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
    RoleRepository mockRoleRepository;

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
    public void update_Should_CallRepository_WhenUserIsAdmin() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Role mockRole = Helpers.createMockAdminRole();

        Mockito.doNothing().when(mockRepository).update(mockPost);

        Mockito.when(mockRoleRepository.getByName("admin")).thenReturn(mockRole);

        mockUser.getRoles().add(mockRole);

        //Act
        service.update(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }

    @Test
    public void update_Should_CallRepository_WhenUserIsCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();

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

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.update(mockPost, mockUser));
    }

    @Test
    public void delete_Should_CallRepository_WhenUserIsAdmin() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Role mockRole = Helpers.createMockAdminRole();

        Mockito.when(service.getById(Mockito.anyInt())).thenReturn(mockPost);

        Mockito.when(mockRoleRepository.getByName("admin")).thenReturn(mockRole);

        mockUser.getRoles().add(mockRole);

        //Act
        service.delete(Mockito.anyInt(), mockUser);

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

        Mockito.when(service.getById(Mockito.anyInt())).thenReturn(mockPost);

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(Mockito.anyInt(), mockUser));
    }

    @Test
    public void likePost_Should_Throw_WhenPostDoesNotExist() throws Exception {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.likePost(2, mockUser));
    }

    @Test
    public void likePost_Should_CallLikeServiceToDelete_WhenLikeExist() throws Exception {
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
    public void likePost_Should_CallLikeServiceToCreate_WhenLikeDoesNotExist() throws Exception {
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
}
