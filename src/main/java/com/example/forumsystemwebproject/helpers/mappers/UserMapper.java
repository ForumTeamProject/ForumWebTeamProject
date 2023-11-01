package com.example.forumsystemwebproject.helpers.mappers;


import com.example.forumsystemwebproject.models.DTOs.UserDto;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService service;

    @Autowired
    public UserMapper(UserService service) {
        this.service = service;
    }

    public User fromDto(int id, UserDto dto) {
        User user = fromDto(dto);
        user.setId(id);
        User repositoryUser = service.getById(id);
//        user.setRole(repositoryUser.getRole());
        return user;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
//        user.getRole().setName("user");
        return user;
    }

}
