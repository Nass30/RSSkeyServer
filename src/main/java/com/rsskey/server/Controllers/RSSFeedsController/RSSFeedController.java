package com.rsskey.server.Controllers.RSSFeedsController;

import com.rsskey.server.Controllers.APIError;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.Category;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.User;
import com.rsskey.server.RSSParser.RSSFeedParser;
import com.rsskey.server.Repository.RssFeedRepository;
import com.rsskey.server.Utils.TokenAuth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.lang.Long;

@RestController
@RequestMapping(path="/feeds")
public class RSSFeedController {

    @RequestMapping(value="/user", method = RequestMethod.GET)
    public ResponseEntity getAllFeedsUser(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        List array = null;
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        array = DAOFactory.getInstance().getRssFeedRepository().getRSSFeed(user.getID());
        return new ResponseEntity(array, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllFeeds(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        RSSFeed feed = new RSSFeed("Title", "link", "description", "language", "copyright", "date", new Long(01));
        List array = new ArrayList<RSSFeed>();
        array.add(feed);
        return new ResponseEntity(array, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public ResponseEntity putFeed(@RequestBody RSSFeedPutRequestBody url, @RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        try {
            RSSFeedParser rssFeedParser = new RSSFeedParser(url.url);
            RSSFeed rssFeed = rssFeedParser.readFeed();
            DAOFactory.getInstance().getRssFeedRepository().add(rssFeed);
            return new ResponseEntity<>(rssFeed, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(new APIError(HttpStatus.BAD_REQUEST, e), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFeed(@PathVariable("id") Long id, @RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getFeed(Long id, @RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(null);
    }

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

    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public ResponseEntity getFavorites(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        List<Category> categories = new ArrayList<Category>();
        categories.add(new Category("Tech", new ArrayList(), new Long(32)));
        categories.add(new Category("Food", new ArrayList(), new Long(33)));
        return new ResponseEntity(categories, HttpStatus.OK);
    }

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
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/favorites/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFavorite(@RequestHeader("token") String token, @PathVariable("id") Long id) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (id == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing id of the RssFeed"), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(null);
    }

}
