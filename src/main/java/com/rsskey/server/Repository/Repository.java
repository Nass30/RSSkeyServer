package com.rsskey.server.Repository;

import com.rsskey.server.DatabaseUtils.DbConnect;

import java.sql.Connection;

public abstract class Repository<T> {

    protected Connection connect = null;
    protected DbConnect dbconnect= null;

    public Repository(DbConnect conn) {
        this.dbconnect = conn;
        this.connect = conn.getCon();
    }

    public abstract T add(T model);
    public abstract Boolean delete(int ID);
    public abstract T findbyID(int ID);
    public abstract T update(T model);
}
