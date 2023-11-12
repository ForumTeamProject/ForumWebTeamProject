package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Tag;

import java.util.List;

public interface PostRepository {

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByUserId(PostFilterOptions filterOptions, int id);
    List<Like> getLikes(int postId);

    List<Post> getMostCommented();

    List<Post> getMostRecentlyCreatedPosts();

    Post getById(int id);

    void addTagToPost(Post post, Tag tag);

    void addTagsToPost(Post post, List<Tag> tags);

    void deleteTagFromPost(Post post, Tag tag);

    long getCount();

    void create(Post post);

    void update(Post post);

    void delete(Post post);
}
