package com.example.forumsystemwebproject.helpers.filters;

import com.example.forumsystemwebproject.models.DTOs.PostFilterDto;

import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> username;

    private Optional<String> title;

    private Optional<String> sortBy;
    private Optional<String> sortOrder;

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
