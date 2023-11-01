package com.example.forumsystemwebproject.repositories.contracts;

import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Tag;

import java.util.List;

public interface PostTagRepository {
    void createPostTagsAssociation(Post post, List<Tag> tags);

    void createPostTagsAssociation(int post_id, List<String> tags_content);

    void deletePostTagAssociation(Post post, List<Tag> tags);

    void deletePostTagAssociation(Post post, Tag tag);

    void addTagToPost(Post post, Tag tag);
}
