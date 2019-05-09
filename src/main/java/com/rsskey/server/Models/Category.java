package com.rsskey.server.Models;

import java.util.List;

public class Category {
    public String title;
    public List<Long> feeds;

    public Category(String title, List<Long> feeds) {
        this.title = title;
        this.feeds = feeds;
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
