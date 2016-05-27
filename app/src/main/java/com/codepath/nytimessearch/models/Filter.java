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
    String query;
    Date beginDate;
    String sortOrder;
    String newsType;
    int page;

    public Filter () {
        query = "";
        beginDate = new Date();
        sortOrder = "newest";
        newsType = "";
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void resetPage() {
        this.page = 0;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        resetPage();
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
        resetPage();
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
        resetPage();
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
        resetPage();
    }
}
