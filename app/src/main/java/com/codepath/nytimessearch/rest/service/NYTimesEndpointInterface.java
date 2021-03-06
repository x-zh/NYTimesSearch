package com.codepath.nytimessearch.rest.service;

import com.codepath.nytimessearch.rest.models.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by charlie_zhou on 5/25/16.
 */
public interface NYTimesEndpointInterface {
    static String apiKey = "3b2d7c8a2e964f398c391accd692a117";

    @GET("svc/search/v2/articlesearch.json?api-key=" + apiKey + "&fl=web_url,multimedia,headline")
    Call<List<Article>> searchArticle(@Query("q") String keyword);

    @GET("svc/search/v2/articlesearch.json?api-key=" + apiKey + "&fl=web_url,multimedia,headline")
    Call<List<Article>> searchArticle(@Query("q") String keyword,
                                      @Query("begin_date") String beginDate,
                                      @Query("sort") String sortType,
                                      @Query("fq") String newsType,
                                      @Query("page") int page);
}
