package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.User;
import com.rsskey.server.Utils.SQLHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends ARepository<User> {

    public UserRepository(DAOFactory dao) {
        super(dao);
    }

    @Override
    public User add(User model) throws DAOException {
        Boolean resultQuery = false;
        User DBModel = null;
        ArrayList<Long> result = null;
        String query = "INSERT INTO public.users(\"Login\", \"Password\", \"Email\") VALUES (?, ?, ?)";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true,"LoginID", model.getLogin(), model.getPassword(), model.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result != null && result.size() > 0)
                DBModel = this.findbyID(result.get(0));
        }
        return DBModel;
    }

    @Override
    public Boolean delete(Long ID) {
        Boolean toreturn = false;
        ArrayList<Long>  result = null;
        String query = "DELETE FROM public.users WHERE \"LoginID\"=?";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true, "LoginID", ID);
            if (result != null && result.size() > 0)
                toreturn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    @Override
    public User findbyID(Long ID) {
        User user = new User();
        User newUser = null;
        String query = "SELECT * FROM public.users WHERE \"LoginID\"=?";

        try {
            newUser = (User)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, user ,ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }

    public User findByLoginPass(String login, String password) {
        User user = new User();
        User findedUser = null;
        String query = "SELECT * FROM public.users WHERE \"Login\" = ? AND \"Password\" = ?";

        try {
            findedUser = (User)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, user ,login, password);
            System.out.print(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findedUser;
    }


    public User findByToken(String token) {
        User user = new User();
        User findedUser = null;
        String query = "SELECT * FROM public.users WHERE \"Token\" = ?";

        try {
            findedUser = (User)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, user , token);
            System.out.print(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findedUser;
    }

    public User findbyLogin(String Login) {
        User user = new User();
        User findedUser = null;
        String query = "SELECT * FROM public.users where \"Login\"=?";

        try {
            findedUser = (User)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, user ,Login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findedUser;
    }

    public User findbyMail(String Mail) {
        User user = new User();
        User findedUser = null;
        String query = "SELECT * FROM public.users where \"Email\"=?";

        try {
            findedUser = (User)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, user, Mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findedUser;
    }

    @Override
    public User update(User model) {

        ArrayList<Long> result = null;
        User updatedModel = null;
        String query = "UPDATE public.users SET \"Login\"=?, \"Password\"=?, \"Email\"=?, \"Token\"=?  WHERE \"LoginID\"=?";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(), query,false, null, model.getLogin(), model.getPassword(), model.getEmail(), model.getToken(), model.getID());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (result != null && result.size() > 0)
                updatedModel = this.findbyID(result.get(0));
        }
        return updatedModel;
    }

    public List<RSSFeed> getRSSFeed(int IDUser)
    {
        return null;
    }
}
