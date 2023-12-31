package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
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

            filterOptions.getUser().ifPresent(value -> {
                filters.add("user.username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            filterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Post> getByUserId(PostFilterOptions filterOptions, int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = userRepository.getById(id);
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

    public List<Like> getLikes(int postId) {

        try (Session session = sessionFactory.openSession()) {
            Post post = getById(postId);
            String queryString = "Select l  from Like l where l.post = :post";
            Query<Like> query = session.createQuery(queryString, Like.class)
                    .setParameter("post", post);
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
    public List<Post> getMostCommented() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("""
                SELECT p
                FROM Post p
                LEFT JOIN Comment c ON p.id = c.post.id
                GROUP BY p
                ORDER BY COUNT(c.id) DESC
                """, Post.class)
                    .setMaxResults(10); // Limit the result to 10

            return query.list();
        }
    }

    @Override
    public List<Post> getMostRecentlyCreatedPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("""
                    FROM Post p
                    ORDER BY p.creationDate DESC
                    LIMIT 10
                    """, Post.class);

            return query.list();
        }
    }

    @Override
    public long getCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select COUNT(p) from Post p", Long.class);
            return query.uniqueResult();
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
            processCommentRemoval(session, post);
            processLikesRemoval(session,post);
            session.remove(post);
            session.getTransaction().commit();
        }
    }

//    @Override
//    public void addTagToPost(Post post, Tag tag) {
//        Set<Tag> refTags = post.getTags();
//        refTags.add(tag);
//    }
//
//    @Override
//    public void addTagsToPost(Post post, List<Tag> tags) {
//        Set<Tag> refTags = post.getTags();
//        refTags.addAll(tags);
//    }
//
//    @Override
//    public void deleteTagFromPost(Post post, Tag tag) {
//        Set<Tag> refTags = post.getTags();
//        refTags.remove(tag);
//    }

    private String generateOrderBy(PostFilterOptions postFilterOptions) {
        if (postFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";

        switch (postFilterOptions.getSortBy().get()) {
            case "username":
                orderBy = "user.username";
                break;
            case "title":
                orderBy = "title";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (postFilterOptions.getSortOrder().isPresent() && postFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

    private void processLikesRemoval(Session session, Post post) {
        String query = "Delete from Like l where l.post = :post";
        session.createQuery(query).setParameter("post", post)
                .executeUpdate();
    }

    private void processCommentRemoval(Session session, Post post) {
        String query = "Delete from Comment c where c.post = :post";
        session.createQuery(query).setParameter("post", post)
                .executeUpdate();
    }
}