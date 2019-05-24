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
    private String image;
    private String pubdate;
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
        this.image = null;
        this.pubdate = null;
    }

    public RSSFeedItem(String guid, String title, String description, String link, String author, String image, String pubdate, Long ID, Long rssID) {
        this.guid = guid;
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.image = image;
        this.pubdate = pubdate;
        this.ID = ID;
        this.rssID = rssID;
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
                ", image='" + image + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    @Override
    public boolean equals(Object obj) {
        RSSFeedItem other = (RSSFeedItem)obj;
        if (other == null) {
            System.out.println("Can't cast other to compare");
            return false;
        }
        return (this.guid.equals(other.guid) &&
                this.link.equals(other.link));
    }

    @Override
    public int hashCode() {
        if (this.guid != null) {
            return this.guid.hashCode();
        } else if (this.link != null) {
            return this.link.hashCode();
        } else if (this.description != null) {
            return this.description.hashCode();
        } else {
            return this.author.hashCode();
        }
    }

    @Override
    public RSSFeedItem map(ResultSet resultSet) throws SQLException {
        this.setRssID( resultSet.getLong( "RssID" ) );
        this.setGuid( resultSet.getString( "Guid" ) );
        this.setAuthor( resultSet.getString( "Author" ) );
        this.setLink( resultSet.getString( "Link" ) );
        this.setDescription( resultSet.getString( "Description" ) );
        this.setID( resultSet.getLong( "RssItemID" ));
        this.setTitle(resultSet.getString("Title"));
        this.setImage(resultSet.getString("Image"));
        this.setPubdate(resultSet.getString("PubDate"));
        RSSFeedItem res = new RSSFeedItem(this.guid, this.title, this.description, this.link, this.author, this.image, this.pubdate, this.ID, this.rssID);
        return res;
    }
}
