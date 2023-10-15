package com.example.forumsystemwebproject.helpers;

import java.util.Optional;

public class FilterOptions {

    private Optional<String> user;

    private Optional<String> title;

    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions(
        String user,
        String title,
        String sortBy,
        String sortOrder
    ) {
        this.user = Optional.ofNullable(user);
        this.title = Optional.ofNullable(title);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getUser() {
        return user;
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
