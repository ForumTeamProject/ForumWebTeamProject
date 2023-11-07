package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.FileOperationException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import com.example.forumsystemwebproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final RoleRepository roleRepository;

    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public List<User> get(UserFilterOptions filterOptions) {
        return repository.get(filterOptions);
    }


    @Override
    public User getByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    @Override
    public void create(User user) {
        checkUsernameUniqueness(user);
        checkEmailUniqueness(user);
        repository.create(user);
    }

    @Override
    public void update(User userToUpdate, User authenticatedUser) {
        authorizationHelper.creatorCheck(authenticatedUser, userToUpdate);
        checkEmailUniqueness(userToUpdate);
        repository.update(userToUpdate);
    }

    @Override
    public void delete(User user, int id) {
        User userToDelete = getById(id);
        User deletedUser = getById(999);
        authorizationHelper.creatorCheck(user, userToDelete);

        repository.delete(userToDelete, deletedUser);
    }

    @Override
    public void blockOrUnblockUser(User user, User userToUpdate) {
        try {
            authorizationHelper.blockedCheck(userToUpdate);
            userToUpdate.getRoles().add(roleRepository.getByName("blockedUser"));
            repository.update(userToUpdate);
        } catch (UnauthorizedOperationException e) {
            for (Role role : userToUpdate.getRoles()) {
                if (role.getName().equals("blockedUser")) {
                    userToUpdate.getRoles().remove(role);
                    repository.update(userToUpdate);
                }
            }
        }
    }

//    public void addPhoneNumber(User user, PhoneNumber number) {
//
//    }

    private void checkUsernameUniqueness(User user) {
        boolean duplicateExists = true;
        try {
            repository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
    }


    private void checkEmailUniqueness(User user) {
        boolean duplicateExists = true;
        try {
            User userToCheck = repository.getByEmail(user.getEmail());
            if (userToCheck.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
    }

    public String saveUploadedFileAndGetUrl(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Define the directory where you want to save the uploaded file
                String uploadDir = "classpath:static/assets/img/users/";
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                // Create the directory if it doesn't exist
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                // Save the file to the specified directory
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Return the URL where the file is saved (this URL can be used to retrieve the image)
                return "/assets/img/users/" + fileName;  // This can be the relative path to the image
            } catch (IOException e) {
                throw new FileOperationException("An error with uploading the image occurred");
            }
        } else {
            return null;
        }
    }

}
