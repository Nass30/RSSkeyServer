package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.RSSFeedItem;
import com.rsskey.server.Utils.SQLHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RssFeedItemRepository extends ARepository<RSSFeedItem> {

    public RssFeedItemRepository(DAOFactory dao) {
        super(dao);
    }

    public RSSFeedItem add(RSSFeedItem model) {
        String query = "INSERT INTO public.rssfeeditem(\"Guid\", \"Description\", \"Link\", \"Title\", \"Author\", \"RssID\") VALUES (?, ?, ?, ?, ?, ?);";
        RSSFeedItem DBModel = null;
        ArrayList<Long> result = null;

        try {
            Timestamp ts = new Timestamp(new Date().getTime());

            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true, "RssItemID", model.getGuid(),
                    model.getDescription(), model.getLink(), model.getTitle(), model.getAuthor(), model.getRssID());
            if (result != null && result.size() > 0) {
                DBModel = this.findbyID(result.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return DBModel;
    }

    @Override
    public Boolean delete(Long ID) throws DAOException {
        Boolean toreturn = false;
        ArrayList<Long>  result = null;
        String query = "DELETE FROM public.rssfeeditem WHERE \"RssItemID\"=?";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false, null, ID);
            if (result != null && result.size() > 0)
                toreturn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public List<RSSFeedItem> getRSSFeedItems(Long id) {
        RSSFeedItem feed = new RSSFeedItem();
        ArrayList<RSSFeedItem> feeds = new ArrayList<RSSFeedItem>();
        String query = "SELECT * FROM public.rssfeeditem where \"RssID\"=?";

        try {
            System.out.println("Find RssFeedItems By RSSID" + id);
            feeds = (ArrayList<RSSFeedItem>)(ArrayList<?>) SQLHelper.executeQuery(this.daoFactory.getConnection(), query, feed ,id);
            System.out.println("Found " + feeds.size() +" items");
        } catch (Exception e) {
            System.out.println("Error :");
            e.printStackTrace();
        }
        return feeds;
    }

    @Override
    public RSSFeedItem findbyID(Long ID) throws DAOException {
        RSSFeedItem feed = new RSSFeedItem();
        RSSFeedItem newFeed = null;
        String query = "SELECT * FROM public.rssfeeditem where \"RssItemID\"=?";

        try {
            System.out.println("Find RssFeedItem By ID");
            newFeed = (RSSFeedItem) SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, feed ,ID);
            System.out.println("Find " + newFeed);
        } catch (Exception e) {
            System.out.println("Error ");
            e.printStackTrace();
        }
        return newFeed;
    }

    public RSSFeedItem update(RSSFeedItem model) {
        RSSFeedItem updatedModel = null;

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
}
