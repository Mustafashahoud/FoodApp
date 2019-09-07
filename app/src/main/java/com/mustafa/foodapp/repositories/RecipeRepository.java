package com.mustafa.foodApp.repositories;

import android.content.Context;
import android.util.Log;

import com.mustafa.foodApp.AppExecutors;
import com.mustafa.foodApp.models.Recipe;
import com.mustafa.foodApp.persistance.RecipeDatabase;
import com.mustafa.foodApp.persistance.RecipeDoa;
import com.mustafa.foodApp.requests.ServiceGenerator;
import com.mustafa.foodApp.requests.responses.ApiResponse;
import com.mustafa.foodApp.requests.responses.RecipeResponse;
import com.mustafa.foodApp.requests.responses.RecipeSearchResponse;
import com.mustafa.foodApp.util.Constants;
import com.mustafa.foodApp.util.NetworkBoundResource;
import com.mustafa.foodApp.util.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private RecipeDoa recipeDoa;

    public static RecipeRepository getInstance(final Context context) {
        if (instance == null) {
            instance = new RecipeRepository(context);
        }

        return instance;
    }

    private RecipeRepository(Context context) {
        recipeDoa = RecipeDatabase.getInstance(context).getRecipeDoa();
    }

    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {

                if (item.getRecipes() != null) { // Recipe list will be null if the api key is expired
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];
                    int index = 0;

                    for (long rowId : recipeDoa.insertRecipes((Recipe[]) (item.getRecipes().toArray(recipes)))) {
                        if (rowId == -1) {
                            Log.d(TAG, "saveCallResult: CONFLICT... This photo is already in the cache");
                            //if the recipe already exists ... I don't want to set the ingredients or timestamp
                            // They will be erased
                            recipeDoa.updateRecipe(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );
                        }

                        index++;

                    }


                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDoa.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator
                        .getRecipeApi()
                        .searchRecipe(
                                Constants.API_KEY,
                                query,
                                String.valueOf(pageNumber));
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Recipe>> searchRecipeApi(final String recipeId) {
        return new NetworkBoundResource<Recipe, RecipeResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull RecipeResponse item) {

                // Will be null if API key is expired
                if (item.getRecipe() != null) {
                    item.getRecipe().setTimestamp((int) (System.currentTimeMillis() / 1000));
                    recipeDoa.insertRecipe(item.getRecipe());
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable Recipe data) {
                // here is complacted cuz i wanna look at the timestamp  and determine
                // if it going tp refresh it (Get it form the network)
                int currentTime = (int) (System.currentTimeMillis() / 1000);
                int refreshTime = data.getTimestamp();

                if ((currentTime - data.getTimestamp()) >= Constants.RECIPE_REFRESH_TIME) {
                    return true;
                }
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Recipe> loadFromDb() {
                return recipeDoa.getRecipe(recipeId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().getRecipe(Constants.API_KEY, recipeId);
            }
        }.getAsLiveData();
    }

}
