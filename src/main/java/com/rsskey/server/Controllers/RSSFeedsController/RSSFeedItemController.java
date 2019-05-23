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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/feedsItem")
public class RSSFeedItemController {

    /*
     List all favorites feeds.
     */
    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public ResponseEntity getFavorites(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }

        RssFeedItemRepository repo = DAOFactory.getInstance().getRssFeedItemRepository();
        return new ResponseEntity(repo.getFavoriteFeeds(user.getID()), HttpStatus.OK);
    }

    /*
     Add a feed Item in favorites.
     */
    @RequestMapping(value = "/favorites/add/{id}", method = RequestMethod.POST)
    public ResponseEntity addFavorites(@RequestHeader("token") String token, @PathVariable("id") Long id) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        /*if (rssFeed.getID() == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing ID of the Rssfeed"), HttpStatus.BAD_REQUEST);
        }*/
        RssFeedItemRepository repo = DAOFactory.getInstance().getRssFeedItemRepository();
        if (repo.findbyID(id) == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Unknown RssFeed Id "+ id), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(repo.addFavorite(user.getID(),id));
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
        RssFeedItemRepository repo = DAOFactory.getInstance().getRssFeedItemRepository();
        if (repo.findbyID(id) == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing id of the RssFeed"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(repo.removeFavorite(user.getID(),id));
    }

}
