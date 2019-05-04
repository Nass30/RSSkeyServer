package com.rsskey.server.Repository;

import com.rsskey.server.DatabaseUtils.DbConnect;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Utils.SQLHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RssFeedRepository extends Repository<RSSFeed> {

    public RssFeedRepository(DbConnect dbconnection) {
        super(dbconnection);
    }

    public RSSFeed add(RSSFeed model) {
        Boolean resultQuery = false;
        RSSFeed result = null;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "INSERT INTO public.rssfeed(\"Title\", \"Link\", \"Description\", \"Language\", \"Copyright\", \"Pubdate\") VALUES (?, ?, ?, ?, ?, ?)";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);

            //On remplace le premier trou par le nom du professeur
            prepare.setString(1, model.getTitle());
            prepare.setString(2, model.getLink());
            prepare.setString(3, model.getDescription());
            prepare.setString(4, model.getLanguage());
            prepare.setString(5, model.getCopyright());
            Date date= new Date();

            long time = date.getTime();
            System.out.println("Time in Milliseconds: " + time);

            Timestamp ts = new Timestamp(time);

            prepare.setTimestamp(6, ts);

            resultQuery = prepare.execute();
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultQuery)
                result = this.find("Link", model.getLink());
        }
        return result;
    }

    public Boolean delete(int ID) {
        Boolean result = false;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String query = "DELETE FROM public.rssfeed WHERE \"RssID\"=?";
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

    public RSSFeed findbyID(int ID) {
        RSSFeed findedModel = null;

        try {
            findedModel = this.find("RssID", ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findedModel;
    }



    public RSSFeed update(RSSFeed model) {
        RSSFeed updatedModel = null;

        try {
            Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "UPDATE public.users SET \"Title\"=?, \"Link\"=?, \"Description\"=?, \"Language\"=?, \"Copyright\"=?, \"Pubdate\"=? WHERE \"RssID\"=?";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);

            prepare.setString(1, model.getTitle());
            prepare.setString(2, model.getLink());
            prepare.setString(3, model.getDescription());
            prepare.setString(4, model.getLanguage());
            prepare.setString(5, model.getCopyright());
            Date date= new Date();

            long time = date.getTime();
            System.out.println("Time in Milliseconds: " + time);

            Timestamp ts = new Timestamp(time);

            prepare.setTimestamp(6, ts);
            prepare.setInt(7, model.getID());

            ResultSet res =  prepare.executeQuery();
            while(res.next()){
                updatedModel = this.findbyID(model.getID());
            }

            res.close();
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedModel;
    }

    private RSSFeed find(String property, Object value){
        String query = "SELECT * FROM public.rssfeed WHERE \" TEST \"=?";
        Map<Integer, Object> params = new HashMap<>();
        RSSFeed rss = null;

        query = SQLHelper.stringMakerByAttr(query,property);
        params.put(1, value);
        List<Map<String, Object>> models = SQLHelper.ExecSqlCommand(query, params);

        if (models.size() > 0 && models.get(0).size() == 7) {
            Map<String, Object> props = models.get(0);
            rss = new RSSFeed(props.get("TITLE").toString(),props.get("LINK").toString(),props.get("DESCRIPTION").toString(),props.get("LANGUAGE").toString(),props.get("COPYRIGHT").toString(),props.get("PUBDATE").toString(),(Integer) props.get("RSSID"));
        }
        return rss;
    }

    public List<RSSFeed> getRSSFeed(int IDUser)
    {
        return null;
    }
}
