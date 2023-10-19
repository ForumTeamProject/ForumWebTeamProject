package com.example.forumsystemwebproject.helpers;

import javax.swing.text.html.Option;
import java.util.Optional;

public class CommentFilterOptions {

    Optional<String> user;

    Optional<String> content;

    Optional<String> sortBy;

    Optional<String> sortOrder;


    public CommentFilterOptions(
            String user,
            String content,
            String sortBy,
            String sortOrder
    ) {
        this.user = Optional.ofNullable(user);
        this.content = Optional.ofNullable(content);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }


    public Optional<String> getUser() {
        return user;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
