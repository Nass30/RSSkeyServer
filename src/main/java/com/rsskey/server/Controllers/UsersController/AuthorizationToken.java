package com.rsskey.server.Controllers.UsersController;

public class AuthorizationToken {
    public String token;

    public AuthorizationToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
