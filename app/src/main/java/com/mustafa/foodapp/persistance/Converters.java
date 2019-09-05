package com.mustafa.foodApp.persistance;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

/**
 * Convert the String [] ingredients to String "comma separated String " to be able to be saved in the room database
 */
public class Converters {

    @TypeConverter
    public static String fromArrayToString(String[] array){
        Gson gson = new Gson();
        String json = gson.toJson(array);
        return json;
    }

    @TypeConverter
    public static String[] fromString(String string) {
        Type listType = new TypeToken<String[]>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(string, listType);
    }
}
