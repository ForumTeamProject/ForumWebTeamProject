package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from RegisteredUser", User.class);
            //TODO filtering
            return query.list();
        }
    }

    @Override
    public User get(int id) {
        try (Session session = sessionFactory.openSession()) {
            User result = session.get(User.class, id);
            if (result == null) {
                throw new EntityNotFoundException("Beer", id);
            }
            return result;
        }
    }


    @Override
    public User get(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from RegisteredUser where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }


    @Override
    public User findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from RegisteredUser where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User beerToRemove = get(id);
            session.remove(beerToRemove);
            session.getTransaction().commit();
        }
    }
}
