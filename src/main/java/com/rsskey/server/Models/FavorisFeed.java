package com.rsskey.server.Models;

import org.springframework.web.context.request.FacesRequestAttributes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FavorisFeed implements IModel<FavorisFeed> {

    private Long RssID;
    private Long UserID;

    public Long getRssID() {
        return RssID;
    }

    public void setRssID(Long rssID) {
        RssID = rssID;
    }

    public Long getUserID() {
        return UserID;
    }

    public void setUserID(Long userID) {
        UserID = userID;
    }

    public FavorisFeed(Long IDUser, Long RssID) {
        this.UserID = IDUser;
        this.RssID = RssID;
    }

    public FavorisFeed() {
        this.UserID = null;
        this.RssID = null;
    }

    public FavorisFeed(FavorisFeed favFeed) {
        this.UserID = favFeed.getUserID();
        this.RssID = favFeed.getRssID();
    }

    @Override
    public FavorisFeed map(ResultSet resultSet) throws SQLException {
        this.RssID = resultSet.getLong("FeedID");
        this.UserID = resultSet.getLong("LoginID");
        return this;
    }

    @Override
    public FavorisFeed clone() {
        return new FavorisFeed(this);
    }
}
