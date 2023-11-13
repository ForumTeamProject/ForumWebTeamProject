package com.example.forumsystemwebproject.helpers.filters;

import java.util.Optional;

public class PostFilterOptions {

    private final Optional<String> username;

    private final Optional<String> title;

    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;


    public PostFilterOptions(
            String user,
            String title,
            String sortBy,
            String sortOrder
    ) {
        this.username = Optional.ofNullable(user);
        this.title = Optional.ofNullable(title);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getUser() {
        return username;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
