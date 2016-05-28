package com.codepath.nytimessearch.rest.adapters;

import android.util.Log;

import com.codepath.nytimessearch.rest.models.Multimedium;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlie_zhou on 5/27/16.
 */
public class MultimediumListDeserializer implements JsonDeserializer<List<Multimedium>> {
    @Override
    public List<Multimedium> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            List<Multimedium> arr = new ArrayList<Multimedium>();
            for(JsonElement jsonElement : json.getAsJsonArray()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonObject.remove("legacy");
                arr.add((Multimedium) context.deserialize(jsonObject, Multimedium.class));
            }
            return arr;
        } catch (RuntimeException e){
            throw e;
        }
    }
}