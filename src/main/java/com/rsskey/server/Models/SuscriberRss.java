package com.rsskey.server.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuscriberRss  implements IModel<SuscriberRss>{

    private Long ID;
    private Long userID;
    private Long rssID;


    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getRssID() {
        return rssID;
    }

    public void setRssID(Long rssID) {
        this.rssID = rssID;
    }

    public SuscriberRss() {
        this.ID = null;
        this.userID = null;
        this.rssID = null;
    }

    public SuscriberRss(Long ID, Long userID, Long rssID) {
        this.ID = ID;
        this.userID = userID;
        this.rssID = rssID;
    }

    @Override
    public SuscriberRss map(ResultSet resultSet) throws SQLException {

        this.setID( resultSet.getLong( "SuscriberID" ) );
        this.setRssID( resultSet.getLong( "RssID" ) );
        this.setUserID( resultSet.getLong( "UserID" ) );
        return this;
    }
}
