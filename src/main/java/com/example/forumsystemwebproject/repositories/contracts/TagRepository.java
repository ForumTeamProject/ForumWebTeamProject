package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;

public interface TagRepository {
    Tag getById(int id);
    Tag getByContent(String content);
    void create(Tag tag);

//    void update(Tag tag);
    void delete(int id);
}
