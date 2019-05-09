package com.rsskey.server.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements IModel<User> {

    private Long ID;
    private String login;
    private String password;
    private String email;

    private String token;
    private ArrayList<RSSFeed> RssFeed = new ArrayList<>();


    public User(){
        this.login = null;
        this.ID = null;
        this.password = null;
        this.email = null;
        this.RssFeed = null;
    }

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public User(String login, String email, String password, Long ID) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.ID = ID;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getID() {
        return ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<RSSFeed> getRSSFeed() { return this.RssFeed; }
    public void setRssFeed(ArrayList<RSSFeed> rssFeed) { this.RssFeed = rssFeed; }

    @Override
    public String toString() {

        return "User {" + '\n' +
                "email :" + this.email +
                "password" + this.password +
                "login" + this.login +
                "ID" + this.ID + '\n'
                + "}";
    }

    @Override
    public User map(ResultSet resultSet) throws SQLException {

        System.out.print(resultSet);
        this.setID( resultSet.getLong( "LoginID" ) );
        this.setEmail( resultSet.getString( "Email" ) );
        this.setPassword( resultSet.getString( "Password" ) );
        this.setLogin( resultSet.getString( "Login" ) );
        String token = resultSet.getString( "token" );
        if (!resultSet.wasNull())
            this.setToken(token);
        this.setRssFeed(new ArrayList<RSSFeed>());
        return this;
    }
}
