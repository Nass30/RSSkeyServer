package com.rsskey.server.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RSSFeedItem implements IModel<RSSFeedItem> {

    // MARK: - Properties

    private String guid;
    private String title;
    private String description;
    private String link;
    private String author;
    private Long ID;
    private Long rssID;

    // MARK: Constructor

    public RSSFeedItem() {
        this.guid = null;
        this.title = null;
        this.description = null;
        this.link = null;
        this.author = null;
        this.rssID = null;
        this.ID = null;
    }

    public RSSFeedItem(String guid, String title, String description, String link, String author, Long ID, Long rssID) {
        this.guid = guid;
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.rssID = rssID;
        this.ID = ID;
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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getRssID() {
        return rssID;
    }

    public void setRssID(Long rssID) {
        this.rssID = rssID;
    }

    @Override
    public String toString() {
        return "RSSItem{" +
                "guid='" + guid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", ID='" + ID + '\'' +
                ", IDrss='" + rssID + '\'' +
                '}';
    }

    @Override
    public RSSFeedItem map(ResultSet resultSet) throws SQLException {
        this.setRssID( resultSet.getLong( "RssID" ) );
        this.setGuid( resultSet.getString( "Guid" ) );
        this.setAuthor( resultSet.getString( "Author" ) );
        this.setLink( resultSet.getString( "Link" ) );
        this.setDescription( resultSet.getString( "Description" ) );
        this.setID( resultSet.getLong( "RssItemID" ) );
        return this;
    }
}
