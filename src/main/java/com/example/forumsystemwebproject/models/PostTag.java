package com.example.forumsystemwebproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "posts_tags")
public class PostTag {
    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PostTag(){

    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
