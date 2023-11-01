package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.LikeRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private final SessionFactory sessionFactory;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Autowired
    public LikeRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository, PostRepository postRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Like get(Post post, User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Like> query = session.createQuery("from Like where post = :post and user = :user", Like.class);
            query.setParameter("post", post);
            query.setParameter("user", user);

            List<Like> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException();
            }
            return result.get(0);
        }
    }

    @Override
    public List<Like> getByUserId(int id) {
        User user = userRepository.getById(id);
        try (Session session = sessionFactory.openSession()) {
            Query<Like> query = session.createQuery("from Like where user = :user", Like.class);
            query.setParameter("user", user);

            return query.list();
        }
    }

    @Override
    public List<Like> getByPostId(int id) {
        Post post = postRepository.getById(id);
        try (Session session = sessionFactory.openSession()) {
            Query<Like> query = session.createQuery("from Like where post = :post", Like.class);
            query.setParameter("post", post);

            return query.list();
        }
    }

    @Override
    public Like getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Like like = session.get(Like.class, id);

            if (like == null) {
                throw new EntityNotFoundException("Like", id);
            }
            return like;
        }
    }

    @Override
    public void create(Like like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(like);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Like like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(like);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Like like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(like);
            session.getTransaction().commit();
        }
    }
}
