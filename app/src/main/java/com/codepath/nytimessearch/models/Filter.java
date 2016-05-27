package com.codepath.nytimessearch.models;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by charlie_zhou on 5/27/16.
 */
@Parcel
public class Filter {
    private String query;
    private Date beginDate;
    private String sortOrder;
    private String newsType;

    public Filter () {
        query = "";
        beginDate = new Date();
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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getBeginDateText() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(beginDate);
    }

    public String getBeginDateToShow() {
        DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        return dateFormat.format(beginDate);
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
