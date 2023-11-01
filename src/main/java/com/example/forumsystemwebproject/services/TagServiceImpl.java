package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.exceptions.UnauthorizedOperationException;
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
import static com.example.forumsystemwebproject.helpers.AuthorizationHelper.authorizeUser;

@Service
public class TagServiceImpl implements TagService {

    private static final String UNAUTHORIZED_ACCESS_MSG = "You do not have permission to edit this post!";
    private final List<Role> authorizationRoles = AuthorizationHelper.makeRoleListFromArgs("admin", "user");
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAll(){  return tagRepository.getAll();}
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
        authorizeUser(user, authorizationRoles);
        boolean duplicateExists = checkDuplicateExists(tag);

        if ((!duplicateExists)) {
            tagRepository.create(tag);
        } else {
            throw new UnauthorizedOperationException(UNAUTHORIZED_ACCESS_MSG);
        }
    }

    @Override
    public void delete(int id, User user) {
      if (AuthorizationHelper.isAdmin(user)){
          tagRepository.delete(id);
      }
      throw new UnauthorizedOperationException(UNAUTHORIZED_ACCESS_MSG);
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
