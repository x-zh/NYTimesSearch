package com.codepath.nytimessearch.models;

import org.parceler.Parcel;

/**
 * Created by charlie_zhou on 5/27/16.
 */
@Parcel
public class Filter {
    private String query;
    private String beginDate;
    private String sortOrder;
    private String newsType;

    public Filter () {
        beginDate = "";
        sortOrder = "newest";
        newsType = "";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
