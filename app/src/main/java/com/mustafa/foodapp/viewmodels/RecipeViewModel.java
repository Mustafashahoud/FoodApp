package com.mustafa.foodapp.viewmodels;

import com.mustafa.foodapp.models.Recipe;
import com.mustafa.foodapp.repositories.RecipeRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository recipeRepository;
    private String mRecipeId;
    private boolean mDidRetrieveRecipe;

    public RecipeViewModel() {
        this.recipeRepository = RecipeRepository.getInstance();
        mDidRetrieveRecipe = false;
    }

    public LiveData<Recipe> getRecipe(){
        return recipeRepository.getRecipe();
    }

    public void getRecipeById (String recipeId){
        mRecipeId = recipeId;
        recipeRepository.getRecipeById(recipeId);
    }

    public String getRecipeId(){
        return mRecipeId;
    }


    // For Recipe timeout
    public MutableLiveData<Boolean> isRecipeRequestTimedOut() {
        return recipeRepository.isRecipeRequestTimedOut();
    }


    public boolean isDidRetrieveRecipe() {
        return mDidRetrieveRecipe;
    }

    public void setDidRetrieveRecipe(boolean mDidRetrieveRecipe) {
        this.mDidRetrieveRecipe = mDidRetrieveRecipe;
    }
}
