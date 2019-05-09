package com.rsskey.server.Controllers.RSSFeedsController;

import com.rsskey.server.Controllers.APIError;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.User;
import com.rsskey.server.RSSParser.RSSFeedParser;
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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity putFeed(@RequestBody RSSFeedPutRequestBody url, @RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        try {
            RSSFeedParser rssFeedParser = new RSSFeedParser(url.url);
            RSSFeed rssFeed = rssFeedParser.readFeed();
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
        return ResponseEntity.ok(null);
    }
}
