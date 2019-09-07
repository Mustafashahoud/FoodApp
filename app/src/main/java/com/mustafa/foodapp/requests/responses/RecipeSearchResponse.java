package com.mustafa.foodApp.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mustafa.foodApp.models.Recipe;

import java.util.List;

import androidx.annotation.Nullable;

public class RecipeSearchResponse {

    @SerializedName("count")
    @Expose()
    private int count;

    @SerializedName("recipes")
    @Expose()
    private List<Recipe> recipes;

    @SerializedName("error")
    @Expose
    private String error;

    public int getCount() {
        return count;
    }

    @Nullable
    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                ", error='" + error + '\'' +
                '}';
    }
}
