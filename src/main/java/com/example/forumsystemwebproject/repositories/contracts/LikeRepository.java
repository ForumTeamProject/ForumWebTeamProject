package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface LikeRepository {

    Like get(Post post, User user);
    List<Like> getByUserId(int id);

    List<Like> getByPostId(int id);

    Like getById(int id);

    void create(Like like);

    void update(Like like);

    void delete(Like like);
}
