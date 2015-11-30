package com.ysy.bean;

import java.util.List;

/**
 * Author: yeshiyuan
 * Date: 11/28/15.
 */
public class NewsDto {
    private List<News> newses;
    private String nextPageUrl ;

    public List<News> getNewses() {
        return newses;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNewses(List<News> newses) {
        this.newses = newses;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }


}
