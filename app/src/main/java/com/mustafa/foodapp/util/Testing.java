package com.mustafa.foodApp.util;

import android.util.Log;

import com.mustafa.foodApp.models.Recipe;

import java.util.List;


public class Testing {

    public static void printRecipes(List<Recipe> list, String tag){
        for(Recipe recipe: list){
            Log.d(tag, "onChanged: " + recipe.getTitle());
        }
    }
}
