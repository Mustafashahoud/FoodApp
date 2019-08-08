package com.mustafa.foodapp.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mustafa.foodapp.models.Recipe;

public class RecipeResponse {
    @SerializedName("recipe")
    @Expose()
    private Recipe recipe;

    public Recipe getRecipe(){
        return recipe;
    }
}
