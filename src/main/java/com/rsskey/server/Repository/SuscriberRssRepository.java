package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.SuscriberRss;
import com.rsskey.server.Utils.SQLHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SuscriberRssRepository extends ARepository<SuscriberRss> {

    public SuscriberRssRepository(DAOFactory dao) {
        super(dao);
    }

    public SuscriberRss add(SuscriberRss model) {
        String query = "INSERT INTO public.suscriberrss(\"UserID\", \"RssID\") VALUES (?, ?)";
        SuscriberRss DBModel = null;
        ArrayList<Long> result = null;

        try {
            Timestamp ts = new Timestamp(new Date().getTime());

            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true, "SuscriberID", model.getUserID(), model.getRssID());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result != null && result.size() > 0)
                DBModel = this.findbyID(result.get(0));
        }
        return DBModel;
    }

    @Override
    public Boolean delete(Long ID) throws DAOException {
        Boolean toreturn = false;
        ArrayList<Long>  result = null;
        String query = "DELETE FROM public.suscriberrss WHERE \"RssID\"=?";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false, null, ID);
            if (result != null && result.size() > 0)
                toreturn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public Boolean deleteByCouple(Long userID, Long rssFeedID) throws DAOException {
        Boolean toreturn = false;
        ArrayList<Long>  result = null;
        String query = "DELETE FROM public.suscriberrss WHERE \"UserID\"=? AND \"RssID\"=?";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(), query,false, null, userID, rssFeedID);
            if (result != null && result.size() > 0)
                toreturn = true;
        } catch (Exception e) {
            System.out.println("[DELETE SUSCRIBER FAILED]");
        }
        return toreturn;
    }

    public SuscriberRss findByCoupleID(Long userID, Long rssID) {
        SuscriberRss suscriberRss = new SuscriberRss();
        String query = "SELECT * FROM public.suscriberrss where \"RssID\"=? and \"UserID\"=?";
        try {
            ArrayList<SuscriberRss> result = (ArrayList<SuscriberRss>)(ArrayList<?>) SQLHelper.executeQuery(this.daoFactory.getConnection(),query, suscriberRss, rssID, userID);
            if (result.size() > 0)
                return result.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SuscriberRss findbyID(Long ID) throws DAOException {
        SuscriberRss feed = new SuscriberRss();
        SuscriberRss newFeed = null;
        String query = "SELECT * FROM public.suscriberrss where \"SuscriberID\"=?";

        try {
            newFeed = (SuscriberRss)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, feed ,ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFeed;
    }

    public SuscriberRss update(SuscriberRss model) {
        return null;
    }

    public ArrayList<Long> getRSSFeed(long IDUser)
    {
        SuscriberRss suscriberRss = new SuscriberRss();
        String query = "SELECT * FROM public.suscriberrss where \"UserID\"=?";
        ArrayList<Long> feedsID = new ArrayList<Long>();

        try {
            System.out.println("Find RssFeed By RSSID" + IDUser);
            feedsID = (ArrayList<Long>)(ArrayList<?>) SQLHelper.executeQuery(this.daoFactory.getConnection(), query, suscriberRss, IDUser);
            System.out.println("Found " + feedsID.size() +" feeds");
        } catch (Exception e) {
            System.out.println("Error :");
            e.printStackTrace();
        }
        return feedsID;
    }
}
