package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.helpers.AuthorizationHelper;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.LikeRepository;
import com.example.forumsystemwebproject.services.contracts.LikeService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public Like get(Post post, User user) {
        return likeRepository.get(post, user);
    }

    @Override
    public List<Like> getByUserId(int id) {
        return likeRepository.getByUserId(id);
    }

    @Override
    public List<Like> getByPostId(int id) {
        return likeRepository.getByPostId(id);
    }

    @Override
    public Like getById(int id) {
        return likeRepository.getById(id);
    }

    @Override
    public void create(Post post, User user) {
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.create(like);
    }

    @Override
    public void update(Like like, User user) {
        likeRepository.update(like);
    }

    @Override
    public void delete(Like like) {
        likeRepository.delete(like);
    }
}
