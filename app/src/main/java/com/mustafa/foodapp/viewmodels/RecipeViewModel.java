package com.mustafa.foodApp.viewmodels;


import android.app.Application;

import com.mustafa.foodApp.models.Recipe;
import com.mustafa.foodApp.repositories.RecipeRepository;
import com.mustafa.foodApp.util.Resource;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;


    public RecipeViewModel(@NonNull Application application) {
        super(application);

        recipeRepository = RecipeRepository.getInstance(application);

    }

    public LiveData<Resource<Recipe>> searchRecipeApi(String rId) {
        return recipeRepository.searchRecipeApi(rId);
    }



}





















