package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.models.Tag;

import java.util.List;

public interface TagRepository {
    Tag getById(int id);

    List<Tag> getAll();

    Tag getByContent(String content);

    Tag getByContentOrCreate(String content);

    void create(Tag tag);

    //    void update(Tag tag);
    void delete(int id);
}
