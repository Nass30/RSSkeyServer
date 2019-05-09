package com.rsskey.server.Models;

import java.util.List;

public class Category {
    public String title;
    public List<Long> feeds;
    public Long id;

    public Category(String title, List<Long> feeds, Long id) {
        this.title = title;
        this.feeds = feeds;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Long> feeds) {
        this.feeds = feeds;
    }
}
