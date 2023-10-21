package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final SessionFactory sessionFactory;

    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Tag result = session.get(Tag.class, id);
            if (result == null) {
                throw new EntityNotFoundException("Tag", id);
            }
            return result;
        }
    }

    @Override
    public Tag getByContent(String content) {
        try (Session session = sessionFactory.openSession()) {
            Tag result = session.get(Tag.class, content);
            if (result == null) {
                throw new EntityNotFoundException("User", "content", content);
            }
            return result;
        }
    }

    @Override
    public void create(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Tag tagToRemove = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(tagToRemove);
            session.getTransaction().commit();
        }
    }
}
