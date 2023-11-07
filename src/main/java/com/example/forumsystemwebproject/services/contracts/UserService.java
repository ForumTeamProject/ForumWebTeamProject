package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User getById(int id);

    List<User> get(UserFilterOptions filterOptions);

    User getByUsername(String username);

    User getByEmail(String email);

    void create(User user);

    void update(User userToUpdate, User authenticatedUser);

    void delete(User user, int id);

    void blockOrUnblockUser(User user, User userToUpdate);

    String saveUploadedFileAndGetUrl(MultipartFile file);
}
