package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Tag;

import java.util.List;

public interface PostRepository {

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByUserId(PostFilterOptions filterOptions, int id);

    List<Post> getMostCommented();

    List<Post> getMostRecentlyCreatedPosts();

    void addTagToPost(Post post, Tag tag);

    void addTagsToPost(Post post, List<Tag> tags);

    void deletePostTagAssociation(Post post, Tag tag);


    Post getById(int id);

    void create(Post post);

    void update(Post post);

    void delete(Post post);
}
