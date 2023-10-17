package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;

import com.example.forumsystemwebproject.helpers.UserFilterOptions;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
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
    public List<RegisteredUser> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<RegisteredUser> query = session.createQuery("from RegisteredUser", RegisteredUser.class);
            //TODO filtering
            return query.list();
        }
    }

    @Override
    public List<RegisteredUser> get(UserFilterOptions userFilterOptions) {
        return null;
    }

    @Override
    public RegisteredUser getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            RegisteredUser result = session.get(RegisteredUser.class, id);
            if (result == null) {
                throw new EntityNotFoundException("User", id);
            }
            return result;
        }
    }

//TODO not sure if these two methods have to exist here. The task is to search by username, email and firstName which will be query params i.e. they will be handled in filter options.
//    @Override
//    public RegisteredUser getByUsername(String username) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<RegisteredUser> query = session.createQuery("from RegisteredUser where username = :username", RegisteredUser.class);
//            query.setParameter("username", username);
//
//            List<RegisteredUser> result = query.list();
//            if (result.isEmpty()) {
//                throw new EntityNotFoundException("User", "username", username);
//            }
//            return result.get(0);
//        }
//    }


//    @Override
//    public RegisteredUser getByEmail(String email) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<RegisteredUser> query = session.createQuery("from RegisteredUser where email = :email", RegisteredUser.class);
//            query.setParameter("email", email);
//
//            List<RegisteredUser> result = query.list();
//            if (result.isEmpty()) {
//                throw new EntityNotFoundException("User", "email", email);
//            }
//            return result.get(0);
//        }
//    }

    @Override
    public void create(RegisteredUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(RegisteredUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(RegisteredUser user) {
        //TODO not sure whether this needs to exist.
    }


//    @Override
//    public void delete(int id) {
//        RegisteredUser userToRemove = getById(id);
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.remove(userToRemove);
//            session.getTransaction().commit();
//        }
//    }
}
