package com.example.sikander.firebasetutorial;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {
    public static <T extends Object> T parseJson(String json, Class<T> className){
        return new GsonBuilder()
                .serializeNulls()
                .create()
                .fromJson(json, className);
    }

    public static <T extends Object> ArrayList<T> parseJsonArray(String data, Type typeToken) {
        return new GsonBuilder()
                .serializeNulls()
                .create()
                .fromJson(data, typeToken);
    }
}
