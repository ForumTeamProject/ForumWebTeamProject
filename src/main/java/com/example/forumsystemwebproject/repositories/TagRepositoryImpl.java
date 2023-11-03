package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Tag> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("FROM Tag", Tag.class);
            return query.list();
        }
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
            Query<Tag> query = session.createQuery("FROM Tag WHERE content = :content", Tag.class);
            query.setParameter("content", content);

            List<Tag> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Tag", "content", content);
            }

            return result.get(0);
        }
    }

    @Override
    public Tag getByContentOrCreate(String content) {
        try (Session session = sessionFactory.openSession()) {
            Tag result = session.get(Tag.class, content);
            if (result == null) {
                Tag tag = new Tag();
                tag.setContent(content);
                create(tag);
                result = getByContent(content);
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
        try (Session session = sessionFactory.openSession()) {
            Tag tagToRemove = getById(id);
            session.beginTransaction();
            session.remove(tagToRemove);
            session.getTransaction().commit();
        }
    }
}
