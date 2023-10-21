package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;

public interface TagService {
    Tag getById(int id);
    Tag getByContent(String content);
    void create(Tag tag, User user);
    void delete(int id, User user);
}
