package com.codepath.nytimessearch.rest;

import com.codepath.nytimessearch.rest.adapters.ArticleListDeserializer;
import com.codepath.nytimessearch.rest.models.Article;
import com.codepath.nytimessearch.rest.service.NYTimesEndpointInterface;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by charlie_zhou on 5/25/16.
 */
public class RestClient {
    private static final String BASE_URL = "https://api.nytimes.com/";
    private NYTimesEndpointInterface apiService;

    public RestClient()
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(new TypeToken<List<Article>>() {}.getType(), new ArticleListDeserializer())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(NYTimesEndpointInterface.class);
    }

    public NYTimesEndpointInterface getApiService()
    {
        return apiService;
    }
}
