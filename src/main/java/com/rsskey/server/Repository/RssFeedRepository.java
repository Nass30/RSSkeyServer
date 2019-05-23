package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.*;
import com.rsskey.server.Utils.SQLHelper;
import java.sql.Timestamp;
import java.util.*;

public class RssFeedRepository extends ARepository<RSSFeed> {

    public RssFeedRepository(DAOFactory dao) {
        super(dao);
    }

    public RSSFeed add(RSSFeed model) {
        String query = "INSERT INTO public.rssfeed(\"Title\", \"Link\", \"Description\", \"Language\", \"Copyright\", \"Pubdate\", rssurl) VALUES (?, ?, ?, ?, ?, ?, ?)";
        RSSFeed DBModel = null;
        ArrayList<Long> result = null;
        RssFeedItemRepository repoRssItem = DAOFactory.getInstance().getRssFeedItemRepository();

        try {
            Timestamp ts = new Timestamp(new Date().getTime());

            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true, "RssID", model.getTitle(), model.getLink(), model.getDescription(), model.getLanguage(), model.getCopyright(), model.getPubDate(), model.getRssURL());
            System.out.println("Result of the query " + result);
            if (result != null && result.size() > 0 && (DBModel = this.findbyID(result.get(0))) != null) {
                System.out.println("Result of the DBModel " + DBModel);
                for (RSSFeedItem rss: model.getItems()) {
                    System.out.println(rss.getGuid());
                    rss.setRssID(DBModel.getID());
                    RSSFeedItem item = repoRssItem.add(rss);
                    DBModel.items.add(item);
                }
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
        String query = "DELETE FROM public.rssfeed WHERE \"RssID\"=?";

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
    public RSSFeed findbyID(Long ID) throws DAOException {
        RSSFeed feed = new RSSFeed();
        RSSFeed newFeed = null;
        String query = "SELECT * FROM public.rssfeed where \"RssID\"=?";
        System.out.println("Find RssFeed by Id" + ID);
        try {
            newFeed = (RSSFeed)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, feed ,ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("newFeed" + newFeed);
        return newFeed;
    }

    public List<RSSFeed> getAllFeeds() throws DAOException {
        RSSFeed feed = new RSSFeed();
        List<RSSFeed> feeds = new ArrayList<RSSFeed>();
        String query = "SELECT * FROM public.rssfeed";

        try {
            feeds = (ArrayList<RSSFeed>)(ArrayList<?>)SQLHelper.executeQuery(this.daoFactory.getConnection(),query, feed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeds;
    }

    @Override
    public RSSFeed update(RSSFeed model) throws DAOException {
        return null;
    }

    public RSSFeed update(RSSFeed oldModel, RSSFeed model) {
        RSSFeed updatedModel = null;
        model.setID(oldModel.getID());
        try {
            String query = "UPDATE public.rssfeed SET \"Title\"=?, \"Link\"=?, \"Description\"=?, \"Language\"=?, \"Copyright\"=?, \"Pubdate\"=? WHERE \"RssID\"=?";
            SQLHelper.executeNonQuery(
                    this.daoFactory.getConnection(),
                    query,
                    false,
                    null,
                    model.getTitle(),
                    model.getLink(),
                    model.getDescription(),
                    model.getLanguage(),
                    model.getCopyright(),
                    model.getPubDate(),
                    model.getID()
            );
            updatedModel = findbyID(model.getID());
            Set<RSSFeedItem> ad = new HashSet<RSSFeedItem>(model.items);
            Set<RSSFeedItem> bd = new HashSet<RSSFeedItem>(oldModel.items);
            Set<RSSFeedItem> updateList = new HashSet<RSSFeedItem>();
            updateList.addAll(bd);
            updateList.addAll(ad);
            updatedModel.items.addAll(updateList);
            ad.removeAll(bd);
            Iterator<RSSFeedItem> itr = ad.iterator();
            RssFeedItemRepository itemsrepo = DAOFactory.getInstance().getRssFeedItemRepository();
            while (itr.hasNext()) {
                RSSFeedItem item = itr.next();
                System.out.println("Item added : GUID: " + item.getGuid() + "Link :" + item.getLink());
                item.setRssID(oldModel.getID());
                itemsrepo.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedModel;
    }

    public List<RSSFeed> getRSSFeeds(long IDUser)
    {
        ArrayList<Long> RssIDS;
        ArrayList<RSSFeed> rssFeeds = new ArrayList<>();
        try {
            RssIDS = DAOFactory.getInstance().getSuscriberRepository().getRSSFeed(IDUser);

            for (Long id: RssIDS) {
                RSSFeed rss = this.findbyID(id);
                if (rss != null)
                    rssFeeds.add(rss);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssFeeds;
    }

    public RSSFeed findByURL(String url) {
        RSSFeed feed = new RSSFeed();
        RSSFeed newFeed = null;
        String query = "SELECT * FROM public.rssfeed where rssurl=?";
        System.out.println("Find RssFeed by URL " + url);

        try {
            newFeed = (RSSFeed)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, feed ,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFeed;
    }

    public Boolean addFavorite(Long IDUser, Long RssID) {

        Boolean queryExecuted = true;
        String query = "INSERT INTO public.FavorisFeeds(\"LoginID\", \"FeedID\") VALUES (?, ?)";

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
        String query = "DELETE FROM public.FavorisFeeds where \"LoginID\"=? and \"FeedID\"=?";

        try {
            SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false,null, IDUser , RssID);
        } catch (Exception e) {
            queryExecuted = false;
            e.printStackTrace();
        }
        return queryExecuted;
    }

    public List<RSSFeed> getFavoriteFeeds(Long IDUser)
    {
        String queryFavFeed = "SELECT * from public.\"rssfeed\" as rf , public.\"FavorisFeeds\" as ff WHERE rf.\"RssID\" = ff.\"FeedID\" and ff.\"LoginID\"=?";
        RSSFeed feed = new RSSFeed();
        ArrayList<RSSFeed> feeds = new ArrayList<>();


        try {
            feeds = (ArrayList<RSSFeed>)(ArrayList<?>)SQLHelper.executeQuery(this.daoFactory.getConnection(),queryFavFeed, feed);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeds;
    }

}
