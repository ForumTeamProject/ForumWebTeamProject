package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.Helpers;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TagServiceTests {

    @Mock
    TagRepository mockTagRepository;
    @Mock
    AuthorizationHelper authorizationHelper;
    @Mock
    AuthorizationHelper mockAuthorizationHelper;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    public void getAll_Should_CallRepository() {
        // Arrange
        Mockito.when(mockTagRepository.getAll()).thenReturn(new ArrayList<>(4));

        // Act
        tagService.getAll();

        // Assert
        verify(mockTagRepository, times(1)).getAll();
    }

    @Test
    public void getById_Should_CallRepository() {
        // Arrange
        int tagId = 1;
        Tag mockTag = new Tag();
        Mockito.when(mockTagRepository.getById(tagId)).thenReturn(mockTag);

        // Act
        Tag result = tagService.getById(tagId);

        // Assert
        verify(mockTagRepository, times(1)).getById(tagId);
        assertEquals(mockTag, result);
    }

    @Test
    public void getByContent_Should_CallRepository() {
        // Arrange
        String content = "Tag Content";
        Tag mockTag = new Tag();
        Mockito.when(mockTagRepository.getByContent(content)).thenReturn(mockTag);

        // Act
        Tag result = tagService.getByContent(content);

        // Assert
        verify(mockTagRepository, times(1)).getByContent(content);
        assertEquals(mockTag, result);
    }

    @Test
    public void create_Should_ReturnExistingTag_WhenTagExists() {
        // Arrange
        Tag mockTag = Helpers.createMockTag();
        String content = mockTag.getContent();
        Mockito.when(mockTagRepository.getByContent(content)).thenReturn(mockTag);

        // Act
        Tag result = tagService.create(Helpers.createMockTag(), Helpers.createMockUser());

        // Assert
        verify(mockTagRepository, Mockito.never()).create(any(Tag.class));
        assertEquals(mockTag, result);
    }

    @Test
    public void create_Should_CallRepository_WhenTagDoesNotExist() {
        // Arrange
        Tag mockTag = Helpers.createMockTag();
        String tagContent = mockTag.getContent();
        User mockUser = Helpers.createMockUser();

        //EntityNotFoundException (simulating tag not existing)
        Mockito.when(mockTagRepository.getByContent(Mockito.any()))
                .thenThrow(EntityNotFoundException.class).thenReturn(mockTag);

        // Act
        tagService.create(mockTag, mockUser);

        // Assert
        Mockito.verify(mockTagRepository, times(1)).create(any(Tag.class));
    }

    @Test
    public void delete_Should_CallRepository_WhenUserIsAdmin() {
        // Arrange
        User mockUser = Helpers.createMockUser();
        Tag mockTag = Helpers.createMockTag();
        int tagId = mockTag.getId();
        mockTag.setId(tagId);
        mockUser.getRoles().add(Helpers.createMockAdminRole());

        // Mockito.when(mockTagRepository.delete(tagId));

        // Act
        tagService.delete(tagId, mockUser);

        // Assert
        verify(mockTagRepository, times(1)).delete(tagId);
    }


//    @Test
//    public void get_Shoudl_ReturnTag_WhenExists(){
//     //Arange
//        Tag tag = Helpers.createMockTag();
//        mockTagRepository.create();
//    }
}
