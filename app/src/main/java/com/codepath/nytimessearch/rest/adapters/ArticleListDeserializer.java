package com.codepath.nytimessearch.rest.adapters;

import com.codepath.nytimessearch.rest.models.Article;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlie_zhou on 5/25/16.
 */
public class ArticleListDeserializer implements JsonDeserializer<List<Article>> {
    @Override
    public List<Article> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            List<Article> arr = new ArrayList<Article>();
            JsonObject response = json.getAsJsonObject().get("response").getAsJsonObject();
            for(JsonElement e : response.get("docs").getAsJsonArray()) {
                arr.add((Article) context.deserialize(e, Article.class));
            }
            return arr;
        } catch (RuntimeException e){
            throw e;
        }
    }
}
