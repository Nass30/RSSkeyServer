package com.rsskey.server.RSSParser;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.RSSFeedItem;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class RSSFeedParser {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String IMAGE = "image";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    final URL url;
    final String feedURLString;

    public RSSFeedParser(String feedUrl) {
        try {
            this.feedURLString = feedUrl;
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public RSSFeed readFeed() {
        RestTemplate restTemplate = new RestTemplate();
        SyndFeed syndFeed = restTemplate.execute(feedURLString, HttpMethod.GET, null, response -> {
            SyndFeedInput input = new SyndFeedInput();
            try {
                return input.build(new XmlReader(response.getBody()));
            } catch (FeedException e) {
                throw new IOException("Could not parse response", e);
            }
        });

        RSSFeed feed = new RSSFeed(
                syndFeed.getTitle(),
                syndFeed.getLink(),
                syndFeed.getDescription(),
                syndFeed.getLanguage(),
                syndFeed.getCopyright(),
                syndFeed.getPublishedDate().toString(),
                feedURLString
        );
        System.out.println("feed parsed");

        for (SyndEntry item: syndFeed.getEntries()) {
            RSSFeedItem rssitem;
            if (item.getEnclosures().size() == 0) {
                rssitem = new RSSFeedItem(item.getUri(), item.getTitle(), item.getDescription().getValue(), item.getLink(), item.getAuthor(), null, item.getPublishedDate().toString(), null, null);
                feed.getItems().add(rssitem);
            } else {
                rssitem = new RSSFeedItem(item.getUri(), item.getTitle(), item.getDescription().getValue(), item.getLink(), item.getAuthor(), item.getEnclosures().get(0).getUrl(), item.getPublishedDate().toString(), null, null);
                feed.getItems().add(rssitem);
            }

        }
        return feed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}