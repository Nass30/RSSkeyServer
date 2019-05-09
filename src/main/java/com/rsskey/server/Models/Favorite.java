package com.rsskey.server.Models;

import java.util.List;

public class Favorite {
    public List<Long> rssFeedsId;

    public Favorite(List<Long> rssFeedsId) {
        this.rssFeedsId = rssFeedsId;
    }

    public List<Long> getRssFeedsId() {
        return rssFeedsId;
    }

    public void setRssFeedsId(List<Long> rssFeedsId) {
        this.rssFeedsId = rssFeedsId;
    }
}
