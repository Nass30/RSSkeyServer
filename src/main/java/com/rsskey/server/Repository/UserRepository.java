package com.rsskey.server.Repository;

import com.rsskey.server.DatabaseUtils.DbConnect;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class UserRepository extends Repository<User>{

    public UserRepository(DbConnect dbconnection) {
        super(dbconnection);
    }

    public User add(User model) {
        Boolean resultQuery = false;
        User result = null;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "INSERT INTO public.users(\"Login\", \"Password\", \"Email\") VALUES (?, ?, ?)";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);


            /*for ( Map.Entry<Integer, Object> entry : params.entrySet() ) {
                String key = entry.getKey();
                Label value = entry.getValue();
                prepare.
            }*/

            //On remplace le premier trou par le nom du professeur
            prepare.setString(1, model.getLogin());
            prepare.setString(2, model.getPassword());
            prepare.setString(3, model.getEmail());

            resultQuery = prepare.execute();
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultQuery)
                result = this.findbyLogin(model.getLogin());
        }
        return result;
    }

    public Boolean delete(int ID) {
        Boolean result = false;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String query = "DELETE FROM public.users WHERE \"LoginID\"=?";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);

            //On remplace le premier trou par le nom du professeur
            prepare.setInt(1, ID);

            result =  prepare.execute();
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public User findbyID(int ID) {
        User findedModel = null;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM public.users where \"loginID \"=?";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);

            prepare.setInt(1, ID);

            ResultSet res =  prepare.executeQuery();
            while(res.next()){
                findedModel = new User(res.getString("Login"),res.getString("Email"),res.getString("Password"), res.getInt("LoginID"));
            }

            res.close();
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return findedModel;
    }

    public User findbyLogin(String Login) {
        User findedModel = null;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "SELECT * FROM public.users where \"login \"=?";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);

            prepare.setString(1, Login);

            ResultSet res =  prepare.executeQuery();
            while(res.next()){
                findedModel = new User(res.getString("Login"),res.getString("Email"),res.getString("Password"), res.getInt("LoginID"));
            }

            res.close();
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return findedModel;
    }

    public User update(User model) {
        User updatedModel = null;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "UPDATE public.users SET \"Login\"=?, \"Password\"=?, \"Email\"=? WHERE \"LoginID\"=?";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);

            prepare.setString(1, model.getLogin());
            prepare.setString(2, model.getPassword());
            prepare.setString(3, model.getEmail());

            ResultSet res =  prepare.executeQuery();
            while(res.next()){
                updatedModel = new User(res.getString("Login"),res.getString("Email"),res.getString("Password"), res.getInt("LoginID"));
            }

            res.close();
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedModel;
    }

    public List<RSSFeed> getRSSFeed(int IDUser)
    {
        return null;
    }
}
