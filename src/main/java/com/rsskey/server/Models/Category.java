package com.rsskey.server.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Category implements IModel<Category> {

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long userID;
    public String category;
    public Long id;

    public Category() {

    }

    public Category(String category, Long userid, Long id) {
        this.category = category;
        this.userID = userid;
        this.id = id;
    }

    @Override
    public Category map(ResultSet resultSet) throws SQLException {
        this.category = resultSet.getString("category");
        this.userID = resultSet.getLong("userID");
        this.id = resultSet.getLong("categoryID");
        return this;
    }
}
