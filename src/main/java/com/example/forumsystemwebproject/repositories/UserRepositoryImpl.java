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
import java.util.stream.Collectors;

@Repository
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

    @Override
    public RegisteredUser getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<RegisteredUser> query = session.createQuery("from RegisteredUser where username = :username", RegisteredUser.class);
            query.setParameter("username", username);
            List<RegisteredUser> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }
    @Override
    public RegisteredUser getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<RegisteredUser> query = session.createQuery("from RegisteredUser where email = :email", RegisteredUser.class);
            query.setParameter("email", email);

            List<RegisteredUser> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(RegisteredUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
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
    public void delete(int id) {
        RegisteredUser userToRemove = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(userToRemove);
            session.getTransaction().commit();
        }
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
private static List<RegisteredUser> filterByUsername(List<RegisteredUser> users, String username) {
    if (username != null && !username.isEmpty()) {
        users = users.stream()
                .filter(user -> containsIgnoreCase(user.getUsername(), username))
                .collect(Collectors.toList());
    }
    return users;
}

    private static List<RegisteredUser> filterByFirstName(List<RegisteredUser> users, String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            users = users.stream()
                    .filter(user -> containsIgnoreCase(user.getFirstName(), firstName))
                    .collect(Collectors.toList());
        }
        return users;
    }

//    private static List<RegisteredUser> filterByLastName(List<RegisteredUser> users, String lastName) {
//        if (lastName != null && !lastName.isEmpty()) {
//            users = users.stream()
//                    .filter(user -> containsIgnoreCase(user.getLastName(), lastName))
//                    .collect(Collectors.toList());
//        }
//        return users;
//      }

//TODO not sure if these two methods have to exist here. The task is to search by username, email and firstName which will be query params i.e. they will be handled in filter options.

    private static List<RegisteredUser> filterByRole(List<RegisteredUser> users, Integer roleId) {
        if (roleId != null) {
            users = users.stream()
                    .filter(user -> user.getRole().getId() == roleId)
                    .collect(Collectors.toList());
        }
        return users;
    }


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

    private static List<RegisteredUser> filterByEmail(List<RegisteredUser> users, String email) {
        //eventually
        if (email != null) {
            users = users.stream()
                    .filter(user -> containsIgnoreCase(user.getEmail(), email))
                    .collect(Collectors.toList());
        }
        return users;
    }

    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
