package com.example.forumsystemwebproject.services.contracts;

import com.example.forumsystemwebproject.helpers.filters.PostFilterOptions;
import com.example.forumsystemwebproject.models.Like;
import com.example.forumsystemwebproject.models.Post;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.models.User;

import java.util.List;

public interface PostService {

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByUserId(PostFilterOptions filterOptions, int id);

    List<Post> getMostCommented();

    List<Like> getLikes(int postId);

    List<Post> getMostRecentlyCreatedPosts();

    void addTagToPost(User userWhoAdds, Post post, Tag tag);

    void deleteTagFromPost(User userWhoDeletes, Post postFromWhichToDelete, Tag tag);

    Post getById(int id);

    void likePost(int id, User user);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id, User user);
}
