package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.CommentFilterOptions;
import com.example.forumsystemwebproject.models.Comment;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
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
public class CommentRepositoryImpl implements CommentRepository{

    private final SessionFactory sessionFactory;

    private final UserRepository userRepository;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> get(CommentFilterOptions filterOptions) {
       try (Session session = sessionFactory.openSession()) {
           List<String> filters = new ArrayList<>();
           Map<String, Object> params = new HashMap<>();

           filterOptions.getUser().ifPresent(value -> {
               filters.add("username like :username");
               params.put("username", value);
           });

           filterOptions.getContent().ifPresent(value -> {
               filters.add("content like :content");
               params.put("content", String.format("%%%s%%", value));
           });


           StringBuilder queryString = new StringBuilder("from Comment");
           if (!filters.isEmpty()) {
               queryString
                       .append(" where ")
                       .append(String.join(" and ", filters));
           }
           queryString.append(generateOrderBy(filterOptions));

           Query<Comment> query = session.createQuery(queryString.toString(), Comment.class);
           query.setProperties(params);
           return query.list();
       }
    }

    @Override
    public List<Comment> getByUserId(CommentFilterOptions filterOptions, int id) {
        try (Session session = sessionFactory.openSession()) {
            RegisteredUser user = userRepository.getById(id);
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getContent().ifPresent(value -> {
                filters.add("content like :content");
                params.put("content", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from Comment where user = :user");
            if (!filters.isEmpty()) {
                queryString
                        .append(" and ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<Comment> query = session.createQuery(queryString.toString(), Comment.class);
            query.setParameter("user", user);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public Comment getById(int id) {
        try (Session session = sessionFactory.openSession()) {
           Comment comment = session.get(Comment.class, id);
           if (comment == null) {
               throw new EntityNotFoundException("Comment", id);
           }
           return comment;
        }
    }

    @Override
    public void create(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(comment);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(CommentFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";

        switch (filterOptions.getSortBy().get()) {
            case "content":
                orderBy = "content";
                break;
            case "username":
                orderBy = "username";
                break;
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
