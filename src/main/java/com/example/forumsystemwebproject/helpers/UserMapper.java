package com.example.forumsystemwebproject.helpers;


import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import com.example.forumsystemwebproject.models.UserModels.UserDto;
import com.example.forumsystemwebproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService service;

    @Autowired
    public UserMapper(UserService service) {
        this.service = service;
    }

    public RegisteredUser fromDto(int id, UserDto dto) {
        RegisteredUser user = fromDto(dto);
        user.setId(id);
        RegisteredUser repositoryUser = service.getById(id);
        user.setRole(repositoryUser.getRole());
        return user;
    }

    public RegisteredUser fromDto(UserDto dto) {
        RegisteredUser user = new RegisteredUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.getRole().setName("user");
        return user;
    }

}
