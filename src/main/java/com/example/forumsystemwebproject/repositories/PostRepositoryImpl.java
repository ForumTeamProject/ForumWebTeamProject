package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    private final UserRepository userRepository;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
    }
    @Override
    public List<Post> get(PostFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            filterOptions.getUser().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", value);
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<Post> query =session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Post> getByUserId(PostFilterOptions filterOptions, int id) {
        try (Session session = sessionFactory.openSession()) {
            RegisteredUser user = userRepository.getById(id);
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from Post where user = :user");
            if (!filters.isEmpty()) {
                queryString
                        .append(" and ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setParameter("user", user);
            query.setProperties(params);
            return query.list();
        }

    }

    @Override
    public Post getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(post);
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(post);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(PostFilterOptions postFilterOptions) {
        if (postFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";

        switch (postFilterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "username":
                orderBy = "username";
                break;
        }

        orderBy = String.format(" order by %s", orderBy);

        if (postFilterOptions.getSortOrder().isPresent() && postFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
