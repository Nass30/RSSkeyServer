package com.rsskey.server.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RSSFeed implements IModel<RSSFeed>{

    private  String title;
    private  String link;
    private  String description;
    private  String language;
    private  String copyright;
    private  String pubDate;
    private  String rssURL;
    private  Long ID;

    public  List<RSSFeedItem> items = new ArrayList<RSSFeedItem>();

    public RSSFeed()
    {
        this.title = null;
        this.link = null;
        this.description = null;
        this.language = null;
        this.copyright = null;
        this.pubDate = null;
        this.ID = null;
        this.rssURL = null;

    }

    public RSSFeed(String title, String link, String description, String language, String copyright, String pubDate, String rssURL) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
        this.rssURL = rssURL;
    }

    public RSSFeed(String title, String link, String description, String language, String copyright, String pubDate, Long ID, String rssURL) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
        this.ID = ID;
        this.rssURL = rssURL;
    }

    public Long getID() {
        return ID;
    }
    public void setID(Long ID) { this.ID = ID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title;}

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link;}

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) { this.copyright = copyright; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public List<RSSFeedItem> getItems() {  return items;  }

    public String getRssURL() {
        return rssURL;
    }

    public void setRssURL(String rssURL) {
        this.rssURL = rssURL;
    }

    @Override
    public String toString() {
        return "RSSFlux{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", copyright='" + copyright + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }

    public boolean equals(RSSFeed other) {
        return (this.rssURL.equals(other.rssURL) &&
                this.title.equals(other.title) &&
                this.link.equals(other.rssURL) &&
                this.language.equals(other.language) &&
                this.copyright.equals(other.copyright) &&
                this.description.equals(other.description) &&
                this.pubDate.equals(other.description));
    }

    @Override
    public RSSFeed map(ResultSet resultSet) throws SQLException {
        this.setTitle(resultSet.getString("Title"));
        this.setID(resultSet.getLong("RssID"));
        this.setCopyright(resultSet.getString("Copyright"));
        this.setDescription(resultSet.getString("Description"));
        this.setPubDate(resultSet.getString("Pubdate"));
        this.setLanguage(resultSet.getString("Language"));
        this.setLink(resultSet.getString("Link"));
        this.setRssURL(resultSet.getString("rssurl"));
        return new RSSFeed(this.title, this.link, this.description, this.language, this.copyright, this.pubDate, this.getID(), this.rssURL);
    }
}
