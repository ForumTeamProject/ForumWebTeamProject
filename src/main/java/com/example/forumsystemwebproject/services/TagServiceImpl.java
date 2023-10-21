package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import com.example.forumsystemwebproject.services.contracts.TagService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.example.forumsystemwebproject.helpers.AuthorizationHelper.authenticateUser;

@Service
public class TagServiceImpl implements TagService {

    private final List<Role> authorizationRoles = AuthorizationHelper.makeRoleListFromArgs("admin", "user");
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag getById(int id) {
        return tagRepository.getById(id);
    }

    @Override
    public Tag getByContent(String content) {
        return tagRepository.getByContent(content);
    }

    @Override
    public void create(Tag tag, User user) {
        authenticateUser(user, authorizationRoles);
        //boolean duplicateExists = checkDuplicateExists(tag);
        tagRepository.create(tag);
    }

    @Override
    public void delete(int id, User user) {
        authenticateUser(user, authorizationRoles);
        tagRepository.delete(id);
    }

    public boolean checkDuplicateExists(Tag tag) {
        boolean duplicateExists = true;
        try {
            tagRepository.getById(tag.getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        return duplicateExists;

    }

}
