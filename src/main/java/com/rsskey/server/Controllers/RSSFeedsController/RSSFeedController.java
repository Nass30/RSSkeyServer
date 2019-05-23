package com.rsskey.server.Controllers.RSSFeedsController;

import com.rsskey.server.Controllers.APIError;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.*;
import com.rsskey.server.RSSParser.RSSFeedParser;
import com.rsskey.server.Repository.RssFeedItemRepository;
import com.rsskey.server.Repository.RssFeedRepository;
import com.rsskey.server.Repository.SuscriberRssRepository;
import com.rsskey.server.Utils.TokenAuth;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.dao.support.DaoSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.lang.Long;

@RestController
@RequestMapping(path="/feeds")
public class RSSFeedController {

    /*
        Random articles from database.
     */
    @RequestMapping(value = "/randompicker", method = RequestMethod.GET)
    public ResponseEntity getRandomArticles() {
        RssFeedItemRepository rssFeedItemRepository = DAOFactory.getInstance().getRssFeedItemRepository();
        List<RSSFeedItem> array = rssFeedItemRepository.getRandomItems();

        return new ResponseEntity(array, HttpStatus.OK);
    }

    /*
     List all feeds.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllFeeds(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        RssFeedRepository rssFeedRepository = DAOFactory.getInstance().getRssFeedRepository();
        List<RSSFeed> array = rssFeedRepository.getAllFeeds();

        return new ResponseEntity(array, HttpStatus.OK);
    }

    /*
     List all feeds of a user.
     */
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public ResponseEntity getAllFeedsUser(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        List array = null;
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        array = DAOFactory.getInstance().getRssFeedRepository().getRSSFeeds(user.getID());
        return new ResponseEntity(array, HttpStatus.OK);
    }

    /*
     Add a feed.
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public ResponseEntity putFeed(@RequestBody RSSFeedPutRequestBody url, @RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        try {
            RssFeedRepository repo = DAOFactory.getInstance().getRssFeedRepository();
            RssFeedItemRepository itemsrepo = DAOFactory.getInstance().getRssFeedItemRepository();
            RSSFeedParser rssFeedParser = new RSSFeedParser(url.url);
            RSSFeed rssFeed = rssFeedParser.readFeed();
            if (rssFeed.getLink() == null || !EmailValidator.getInstance().isValid(rssFeed.getLink())) {
                rssFeed.setLink(url.url);
            }
            RSSFeed dataFeed = repo.findByURL(url.url);
            SuscriberRssRepository suscriberRssRepository = DAOFactory.getInstance().getSuscriberRepository();
            if (dataFeed == null) {
                System.out.println("New feed ! Welcome my man !");
                dataFeed = repo.add(rssFeed);
                suscriberRssRepository.add(new SuscriberRss(null, user.getID(), dataFeed.getID()));
            } else {
                System.out.println("Old feed ! Update it !");
                SuscriberRss suscriberRss = suscriberRssRepository.findByCoupleID(user.getID(), dataFeed.getID());
                if (suscriberRss != null) {
                    return new ResponseEntity(new APIError(HttpStatus.FORBIDDEN, "RssFeed already added to user's feeds."), HttpStatus.FORBIDDEN);
                }
                suscriberRssRepository.add(new SuscriberRss(null, user.getID(), dataFeed.getID()));
                dataFeed.items = itemsrepo .getRSSFeedItems(dataFeed.getID());
                dataFeed = repo.update(dataFeed, rssFeed);
            }

            System.out.println("feeds/user/add : " + dataFeed);
            return new ResponseEntity<>(dataFeed, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(new APIError(HttpStatus.BAD_REQUEST, e), HttpStatus.BAD_REQUEST);
        }
    }

    /*
     Delete a feed in user's data.
     */
    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFeed(@PathVariable("id") Long id, @RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        SuscriberRssRepository suscriberRssRepository = DAOFactory.getInstance().getSuscriberRepository();
        suscriberRssRepository.deleteByCouple(user.getID(), id);
        return ResponseEntity.ok(null);
    }

    /*
        Get a feed from it's id.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getFeed(@PathVariable Long id, @RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (id == null) {
            System.out.println("id null");
            return ResponseEntity.badRequest().body(null);
        }

        RssFeedRepository repo = DAOFactory.getInstance().getRssFeedRepository();
        RSSFeed feed = repo.findbyID(id);
        if (feed == null) {
            return new ResponseEntity(new APIError(HttpStatus.NOT_FOUND, "Feed not found for id " + id), HttpStatus.NOT_FOUND);
        }
        RSSFeedParser rssFeedParser = new RSSFeedParser(feed.getRssURL());
        RSSFeed rssFeed = rssFeedParser.readFeed();
        RssFeedItemRepository itemsrepo = DAOFactory.getInstance().getRssFeedItemRepository();
        feed.items = itemsrepo.getRSSFeedItems(feed.getID());
        System.out.println(feed.items);
        repo.update(feed, rssFeed);

        return new ResponseEntity<>(feed, HttpStatus.OK);
    }

    /*
     List all categories.
     */
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity getCategories(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        List<Category> categories = new ArrayList<Category>();
        categories.add(new Category("Tech", new ArrayList(), new Long(32)));
        categories.add(new Category("Food", new ArrayList(), new Long(33)));
        return new ResponseEntity(categories, HttpStatus.OK);
    }

    /*
     List all feeds for a category.
     */
    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public ResponseEntity getCategoryFeeds(@RequestHeader("token") String token, @PathVariable("id") Long id) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (id == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing id"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new Category("Tech", new ArrayList(), id), HttpStatus.OK);
    }

    /*
     Add a category.
     */
    @RequestMapping(value = "/categories/add", method = RequestMethod.POST)
    public ResponseEntity addCategory(@RequestHeader("token") String token, @RequestBody Category category) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (category.getTitle() == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing title of the category"), HttpStatus.BAD_REQUEST);
        }
        /*if (repo.findCategoryByTitle(category.getTitle()) != null) {
            return new ResponseEntity(new APIError(HttpStatus.FORBIDDEN, "Category Title already created"), HttpStatus.FORBIDDEN);
        }*/

        return new ResponseEntity(new Category("Tech", new ArrayList(), new Long(42)), HttpStatus.OK);
    }

    /*
        Update a category.
     */
    @RequestMapping(value = "/categories/update", method = RequestMethod.PUT)
    public ResponseEntity updateCategory(@RequestHeader("token") String token, @RequestBody Category category) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (category.getId()== null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing id of the category"), HttpStatus.BAD_REQUEST);
        }
        if (category.getTitle() == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing new title of the category"), HttpStatus.BAD_REQUEST);
        }
        /*if (repo.findCategoryById(category.getId()) == null) {
            return new ResponseEntity(new APIError(HttpStatus.FORBIDDEN, "No such Category for Id" + category.getId()), HttpStatus.FORBIDDEN);
        }*/

        return new ResponseEntity(new Category(category.getTitle(), new ArrayList(), category.getId()), HttpStatus.OK);
    }

    /*
     Delete a category.
     */
    @RequestMapping(value = "/categories/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCategory(@RequestHeader("token") String token, @PathVariable Long id) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (id == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing id of the category"), HttpStatus.BAD_REQUEST);
        }
        /*if (repo.findCategoryById(id) == null) {
            return new ResponseEntity(new APIError(HttpStatus.FORBIDDEN, "No such Category for Id" + id), HttpStatus.FORBIDDEN);
        }*/

        return ResponseEntity.ok(null);
    }

    /*
     List all favorites feeds.
     */
    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public ResponseEntity getFavorites(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }

        RssFeedRepository repo = DAOFactory.getInstance().getRssFeedRepository();
        return new ResponseEntity(repo.getFavoriteFeeds(user.getID()), HttpStatus.OK);
    }

    /*
     Add a feed in favorites.
     */
    @RequestMapping(value = "/favorites/add", method = RequestMethod.POST)
    public ResponseEntity addFavorites(@RequestHeader("token") String token, @RequestBody RSSFeed rssFeed) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (rssFeed.getID() == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing title of the Rssfeed"), HttpStatus.BAD_REQUEST);
        }
        RssFeedRepository repo = DAOFactory.getInstance().getRssFeedRepository();
        if (repo.findbyID(rssFeed.getID()) == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Unknown RssFeed Id "+ rssFeed.getID()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(repo.addFavorite(user.getID(),rssFeed.getID()));
    }

    /*
     Delete a feed in favorites.
     */
    @RequestMapping(value = "/favorites/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFavorite(@RequestHeader("token") String token, @PathVariable("id") Long id) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        RssFeedRepository repo = DAOFactory.getInstance().getRssFeedRepository();
        if (repo.findbyID(id) == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing id of the RssFeed"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(repo.removeFavorite(user.getID(),id));
    }

}
