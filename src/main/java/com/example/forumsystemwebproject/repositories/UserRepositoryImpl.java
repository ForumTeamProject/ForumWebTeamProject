package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.filters.UserFilterOptions;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> get(UserFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", value);
            });

            filterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", value);
            });

            filterOptions.getFirstName().ifPresent(value -> {
                filters.add("firstName like :firstName");
                params.put("firstName", value);
            });
            filterOptions.getLastName().ifPresent(value -> {
                filters.add("lastName like :lastName");
                params.put("lastName", value);
            });

            StringBuilder queryString = new StringBuilder("from User");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User result = session.get(User.class, id);
            if (result == null) {
                throw new EntityNotFoundException("User", id);
            }
            return result;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public long getCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT (u) FROM User u", Long.class);
            return query.uniqueResult();
        }
    }


    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
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
            session.persist(user);
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
        User userToRemove = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(userToRemove);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(UserFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";

        switch (filterOptions.getSortBy().get()) {
            case "username":
                orderBy = "username";
                break;
            case "first_name":
                orderBy = "first_name";
            case "last_name":
                orderBy = "last_name";
            case "email":
                orderBy = "email";
                break;
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }


}
