package com.mustafa.foodapp.viewmodels;

import com.mustafa.foodapp.models.Recipe;
import com.mustafa.foodapp.repositories.RecipeRepository;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private boolean isViewingRecipes;
    private boolean isPerformingQuery;

    public RecipeListViewModel() {
        recipeRepository = RecipeRepository.getInstance();
        isPerformingQuery = false;
    }

    /**
     * @return list of recipes
     */
    public LiveData<List<Recipe>> getRecipes(){
        return recipeRepository.getRecipes();
    }

    public LiveData<Boolean> isQueryExhausted(){
        return recipeRepository.isQueryExhausted();
    }

    public void searchRecipesApi(String query, int pageNumber){
        isViewingRecipes = true;
        isPerformingQuery = true;
        recipeRepository.searchRecipesApi(query, pageNumber);
    }

    public void searchNextPage(){
        if (!isPerformingQuery
                && isViewingRecipes
                && !isQueryExhausted().getValue()){
            recipeRepository.searchNextPage();
        }
    }

    public boolean isViewingRecipes() {
        return isViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        this.isViewingRecipes = isViewingRecipes;
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }

    public void setPerformingQuery(boolean performingQuery) {
        isPerformingQuery = performingQuery;
    }

    /**
     *
     * @return true if the app viewing categories Not recipes.
     */
    public boolean onBackPressed(){
        if(isPerformingQuery){
            // Cancel the query
            recipeRepository.cancelRequest();
            isPerformingQuery = false;
        }
        if (isViewingRecipes){
            isViewingRecipes = false;
            return false;
        }
        return true;
    }
}
