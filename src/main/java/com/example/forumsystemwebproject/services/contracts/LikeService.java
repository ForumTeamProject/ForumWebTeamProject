package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface LikeService {

    Like get(Post post, User user);

    List<Like> getByUserId(int id);

    List<Like> getByPostId(int id);

    Like getById(int id);

    void create(Post post, User user);

    void update(Like like, User user);

    void delete(Like like);

}
