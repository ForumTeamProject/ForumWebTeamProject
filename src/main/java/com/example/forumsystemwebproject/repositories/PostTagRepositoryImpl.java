package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.PostTag;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.repositories.contracts.PostRepository;
import com.example.forumsystemwebproject.repositories.contracts.PostTagRepository;
import com.example.forumsystemwebproject.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostTagRepositoryImpl implements PostTagRepository {
    //Create a Post-Tag Association: This method allows you to associate a specific post with one or more tags.
    // It typically takes the post_id and a list of tag_ids as parameters.
    // The repository inserts records into the database table that represents the many-to-many relationship between posts and tags.
    private final SessionFactory sessionFactory;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostTagRepositoryImpl(SessionFactory sessionFactory, PostRepository postRepository, TagRepository tagRepository) {
        this.sessionFactory = sessionFactory;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void addTagToPost(Post post, Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            PostTag relation = new PostTag();
            relation.setPost(post);
            relation.setTag(tag);

            session.persist(relation);
        }

    }

    public void createPostTagsAssociation(Post post, List<Tag> tags) {
        try (Session session = sessionFactory.openSession()) {
            for (Tag tag : tags) {
                PostTag relation = new PostTag();
                relation.setPost(post);
                relation.setTag(tag);

                session.persist(relation);
            }
        }
    }

    public void createPostTagsAssociation(int post_id, List<String> tags_content) {
        try (Session session = sessionFactory.openSession()) {
            Post post = postRepository.getById(post_id);

            for (String tagContent : tags_content) {
                PostTag relation = new PostTag();
                relation.setPost(post);
                relation.setTag(tagRepository.getByContentOrCreate(tagContent));

                session.persist(relation);
            }
        }
    }


    public void deletePostTagAssociation(Post post, List<Tag> tags) {
        try (Session session = sessionFactory.openSession()) {
            for (Tag tag : tags) {
                PostTag relation = new PostTag();
                relation.setPost(post);
                relation.setTag(tag);

                session.beginTransaction();
                session.remove(relation);
                session.getTransaction().commit();
            }
        }
    }

    public void deletePostTagAssociation(Post post, Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            PostTag relation = new PostTag();
            relation.setPost(post);
            relation.setTag(tag);

            session.beginTransaction();
            session.remove(relation);
            session.getTransaction().commit();
        }
    }


    //    public void createPostTagsAssociation(int post_id, List<Integer> tag_ids){
//        try (Session session = sessionFactory.openSession()){
//            Post post = postRepository.getById(post_id);
//
//            for (int tagId: tag_ids) {
//                PostTag relation = new PostTag();
//                relation.setPost(post);
//                relation.setTag(tagRepository.getById(tagId));
//
//                session.persist(relation);
//            }
//        }
//    }

}
