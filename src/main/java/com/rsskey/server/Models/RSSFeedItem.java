package com.rsskey.server.Models;

public class RSSFeedItem {

    // MARK: - Properties

    final String guid;
    final String title;
    final String description;
    final String link;
    final String author;

    // MARK: Constructor

    public RSSFeedItem(String guid, String title, String description, String link, String author) {
        this.guid = guid;
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "RSSItem{" +
                "guid='" + guid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
