package com.example.forumsystemwebproject.models.DTOs;

import com.example.forumsystemwebproject.models.Post;

import java.util.List;

public class PostFilterDto {

    private String user;

    private String title;

    private String sortBy;

    private String sortOrder;

    public PostFilterDto() {

    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
