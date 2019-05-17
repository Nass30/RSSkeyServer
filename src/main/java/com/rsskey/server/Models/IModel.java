package com.rsskey.server.Models;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IModel<T> {
    public T map( ResultSet resultSet ) throws SQLException;
    public T clone();
}
