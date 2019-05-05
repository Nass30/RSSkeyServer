package com.rsskey.server.Models;

public class RSSFeedItem {

    // MARK: - Properties

    private String guid;
    private String title;
    private String description;
    private String link;
    private String author;

    // MARK: Constructor

    public RSSFeedItem(String guid, String title, String description, String link, String author) {
        this.guid = guid;
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
    }

    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) { this.description = description; }

    public String getLink() {
        return link;
    }
    public void setLink(String link) { this.link = link; }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) { this.author = author; }

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
