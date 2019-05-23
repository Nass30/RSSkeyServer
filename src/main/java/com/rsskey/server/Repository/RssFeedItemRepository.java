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
        String query = "INSERT INTO public.rssfeeditem(\"Guid\", \"Description\", \"Link\", \"Title\", \"Author\", \"RssID\", \"Image\", \"PubDate\") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        RSSFeedItem DBModel = null;
        ArrayList<Long> result = null;

        try {
            Timestamp ts = new Timestamp(new Date().getTime());

            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true, "RssItemID", model.getGuid(),
                    model.getDescription(), model.getLink(), model.getTitle(), model.getAuthor(), model.getRssID(), model.getImage(), model.getPubdate());
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

    public List<RSSFeedItem> getRandomItems() {
        RSSFeedItem feed = new RSSFeedItem();
        ArrayList<RSSFeedItem> feeds = new ArrayList<RSSFeedItem>();
        String query = "SELECT * FROM public.rssfeeditem TABLESAMPLE SYSTEM_ROWS(15);";

        try {
            System.out.println("Find random RSSItems");
            feeds = (ArrayList<RSSFeedItem>)(ArrayList<?>) SQLHelper.executeQuery(this.daoFactory.getConnection(), query, feed);
            System.out.println("Found " + feeds.size() +" items");
        } catch (Exception e) {
            System.out.println("Error :");
            e.printStackTrace();
        }
        System.out.println("get Randoms items result");
        System.out.println(feeds);
        return feeds;
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
        System.out.println("getRSSFeedItems result");
        System.out.println(feeds);
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

    public Boolean addFavorite(Long IDUser, Long RssID) {
        Boolean queryExecuted = true;
        String query = "INSERT INTO public.\"FavorisFeedItem\"(\"LoginID\", \"RssItemID\") VALUES (?, ?)";

        try {
            SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false,null, IDUser , RssID);
        } catch (Exception e) {
            queryExecuted = false;
            e.printStackTrace();
        }
        return queryExecuted;
    }

    public Boolean removeFavorite(Long IDUser, Long RssID) {

        Boolean queryExecuted = true;
        String query = "DELETE FROM public.\"FavorisFeedItem\" where \"LoginID\"=? and \"RssItemID\"=?";

        try {
            SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false,null, IDUser , RssID);
        } catch (Exception e) {
            queryExecuted = false;
            e.printStackTrace();
        }
        return queryExecuted;
    }

    public List<RSSFeedItem> getFavoriteFeeds(Long IDUser)
    {
        String queryFavFeed = "SELECT * from public.\"rssfeeditem\" as rfi , public.\"FavorisFeedItem\" as ffi WHERE rfi.\"RssItemID\" = ffi.\"RssItemID\" and ffi.\"LoginID\"=?";
        RSSFeedItem feed = new RSSFeedItem();
        ArrayList<RSSFeedItem> feeds = new ArrayList<>();

        try {
            feeds = (ArrayList<RSSFeedItem>)(ArrayList<?>)SQLHelper.executeQuery(this.daoFactory.getConnection(),queryFavFeed, feed, IDUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeds;
    }

    public RSSFeedItem update(RSSFeedItem model) {
        return null;
    }
}
