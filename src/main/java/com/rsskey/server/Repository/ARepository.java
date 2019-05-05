package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.DatabaseUtils.DbConnect;

import java.sql.Connection;

public abstract class ARepository<T> {

    protected DAOFactory daoFactory;



    public ARepository(DAOFactory dao) {
        this.daoFactory = dao;
    }

    public abstract T add(T model) throws DAOException;
    public abstract Boolean delete(Long ID) throws DAOException;
    public abstract T findbyID(Long ID) throws DAOException;
    public abstract T update(T model) throws DAOException;
}
