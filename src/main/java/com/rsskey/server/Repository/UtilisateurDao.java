package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.Models.User;

public interface UtilisateurDao {

    void create( User utilisateur ) throws DAOException;

    User trouver( String email ) throws DAOException;
}