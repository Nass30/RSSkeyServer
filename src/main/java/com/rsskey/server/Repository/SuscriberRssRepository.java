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

    @Override
    public SuscriberRss findbyID(Long ID) throws DAOException {
        SuscriberRss feed = new SuscriberRss();
        SuscriberRss newFeed = null;
        String query = "SELECT * FROM public.users where \"loginID \"=?";

        try {
            newFeed = (SuscriberRss)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, feed ,ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFeed;
    }

    public SuscriberRss update(SuscriberRss model) {
        SuscriberRss updatedModel = null;

        try {
            /*Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

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
            state.close();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedModel;
    }

    public ArrayList<Long> getRSSFeed(long IDUser)
    {
        RSSFeed feed = new RSSFeed();
        RSSFeed newFeed = null;
        String query = "SELECT RssID FROM public.suscriberrss where \"UserID\"=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Long> RssIDS = new ArrayList<>();
        Connection connexion = null;
        try {
            connexion = this.daoFactory.getConnection();
            preparedStatement = SQLHelper.initialisationRequetePreparee(connexion , query,false,IDUser);
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            while ( resultSet.next() ) {
                RssIDS.add(resultSet.getLong("RssID"));
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            SQLHelper.closeCleanning( resultSet, preparedStatement, connexion);
        }
        return RssIDS;
    }
}
