package com.mustafa.foodapp.requests;

import android.util.Log;

import com.mustafa.foodapp.AppExecutors;
import com.mustafa.foodapp.models.Recipe;
import com.mustafa.foodapp.requests.responses.RecipeResponse;
import com.mustafa.foodapp.requests.responses.RecipeSearchResponse;
import com.mustafa.foodapp.util.Constants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

import static com.mustafa.foodapp.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {
    private RetrieveRecipesRunnable retrieveRecipesRunnable;
    private RetrieveRecipeRunnable retrieveRecipeRunnable;
    private static final String TAG = "RecipeApiClient";
    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> recipes;
    private MutableLiveData<Recipe> recipe;

    private MutableLiveData<Boolean> mRecipeRequestTimeout = new MutableLiveData<>();

    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
        recipe = new MutableLiveData<>();
    }

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public MutableLiveData<Boolean> isRecipeRequestTimedOut() {
        return mRecipeRequestTimeout;
    }


    /**
     * Method to call the actual networking method from Retrofit and fills the LiveData
     * This method will be used in the {@link com.mustafa.foodapp.repositories.RecipeRepository  }
     *
     * @param query      the query that is the searched recipe
     * @param pageNumber the page number
     */
    public void searchRecipeApi(String query, int pageNumber) {

        if (retrieveRecipesRunnable != null) {
            retrieveRecipesRunnable = null;
        }

        retrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);

        //Future is used to set the timeout of the request
        final Future handler = AppExecutors.getInstance().getScheduledExecutor().submit(retrieveRecipesRunnable);

        /* mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm*/
        //Set the timeout, this Runnable is gonna run after 3000 milliSeconds
        AppExecutors.getInstance().getScheduledExecutor().schedule(new Runnable() {
            @Override
            public void run() {
                // Let the user know its timed out
                Log.d(TAG, "run: " + "timedout");
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);

        /* mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm*/
    }


    /**
     * Method to call the actual networking method from Retrofit and fills the LiveData
     * This method will be used in the {@link com.mustafa.foodapp.repositories.RecipeRepository  }
     *
     * @param recipeId the page number
     */
    public void getRecipeById(String recipeId) {

        if (retrieveRecipeRunnable != null) {
            retrieveRecipeRunnable = null;
        }

        retrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);
        final Future handler = AppExecutors.getInstance().getScheduledExecutor().submit(retrieveRecipeRunnable);

        mRecipeRequestTimeout.setValue(false);
        AppExecutors.getInstance().getScheduledExecutor().schedule(() -> {
            //Let the user know it's timeout
            mRecipeRequestTimeout.postValue(true);
            handler.cancel(true);
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }


    /**
     * Class implements Runnable, object of it used in submit to run a task on the background thread.
     */
    private class RetrieveRecipesRunnable implements Runnable {
        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {
                final Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    assert response.body() != null;
                    List<Recipe> list = ((RecipeSearchResponse) response.body()).getRecipes();
                    if (pageNumber == 1) {
                        /////////////////////////////////////////////////////////////////
                        ////////////////////////////////////////////////////////////////
                        /*  Here the LiveData is being set  */
                        recipes.postValue(list);
                    } else {
                        List<Recipe> currentRecipes = recipes.getValue();
                        if (currentRecipes != null) {
                            currentRecipes.addAll(list);
                            recipes.postValue(currentRecipes);
                        } else {
                            recipes.postValue(list);
                        }
                    }
                } else {
                    assert response.errorBody() != null;
                    String error = response.errorBody().string();
                    Log.d(TAG, "run: " + error);
                    recipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                recipes.postValue(null);
            }
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    Constants.API_KEY,
                    query,
                    String.valueOf(pageNumber));
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search");
            cancelRequest = true;
        }
    }


    /**
     * Class implements Runnable, object of it used in submit to run a task on the background thread.
     */
    private class RetrieveRecipeRunnable implements Runnable {
        private String recipeId;
        private boolean cancelRequest;

        RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                final Response response = getRecipe(recipeId).execute();
                if (cancelRequest) {
                    return;
                }

                if (response.code() == 200) {
                    assert response.body() != null;
                    Recipe recipeResponse = ((RecipeResponse) response.body()).getRecipe();

                    /////////////////////////////////////////////////////////////////
                    ////////////////////////////////////////////////////////////////
                    /*  Here the LiveData is being set  */
                    recipe.postValue(recipeResponse);
                } else {
                    assert response.errorBody() != null;
                    String error = response.errorBody().string();
                    Log.d(TAG, "run: " + error);
                    recipes.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                recipes.postValue(null);
            }
        }

        private Call<RecipeResponse> getRecipe(String recipeId) {
            return ServiceGenerator.getRecipeApi().getRecipe(
                    Constants.API_KEY,
                    recipeId);
        }

        public void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search");
            cancelRequest = true;
        }
    }

    public void cancelRequest() {
        if (retrieveRecipeRunnable != null) {
            retrieveRecipeRunnable.cancelRequest();
        }

        if (retrieveRecipesRunnable != null){
            retrieveRecipesRunnable.cancelRequest();
        }
    }
}
