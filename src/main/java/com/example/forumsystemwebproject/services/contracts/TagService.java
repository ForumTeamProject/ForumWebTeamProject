package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface TagService {
    Tag getById(int id);
    List<Tag> getAll();
    Tag getByContent(String content);
    void create(Tag tag, User user);
    void delete(int id, User user);
}
