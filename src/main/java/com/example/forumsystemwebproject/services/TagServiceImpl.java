package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.exceptions.DuplicateEntityException;
import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import com.example.forumsystemwebproject.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagServiceImpl implements TagService {

    private static final String UNAUTHORIZED_ACCESS_MSG = "You do not have permission to edit this post!";
    private final TagRepository tagRepository;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, AuthorizationHelper authorizationHelper) {
        this.tagRepository = tagRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
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
    public Tag create(Tag tag, User user) {
        try {
            return tagRepository.getByContent(tag.getContent());
        } catch (EntityNotFoundException e) {
            tagRepository.create(tag);
            return tagRepository.getByContent(tag.getContent());
        }
    }


    @Override
    public void delete(int id, User user) {
        authorizationHelper.adminCheck(user);
        tagRepository.delete(id);
    }

    public void checkDuplicateExists(Tag tag) {
        boolean duplicateExists = true;
        try {
            tagRepository.getById(tag.getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Tag", "name", tag.getContent());
        }
    }

}
