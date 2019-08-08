package com.mustafa.foodapp.repositories;

import com.mustafa.foodapp.models.Recipe;
import com.mustafa.foodapp.requests.RecipeApiClient;

import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private String mQuery;
    private int mPageNumber;
    private int recipeCountExhausted = 0;
    private int count = 0;
    private int oldListCount;

    //When there is no more search results for our query
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();

    /* TO do so we need something called mediator liveData */
    /*It is used when you want to make a change to a set of liveData before it is returned */
    private MediatorLiveData<List<Recipe>> mMediatorRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance(){
        if (instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    /**
     * Constructor.
     * Instantiates a new Recipe repository.
     */
    public RecipeRepository(){
        recipeApiClient = RecipeApiClient.getInstance();
        initMediator();
    }

    /**
     * Get recipes live data.
     *
     * @return the live data
     */
    public LiveData<List<Recipe>>  getRecipes(){
//        return recipeApiClient.getRecipes();
        /*Cus wanna make a change to it before it is returned from the client*/
        return mMediatorRecipes;
    }

    private void initMediator (){
        LiveData<List<Recipe>> recipeListApiSource = recipeApiClient.getRecipes();
        /*Think about like Mediator is in between section and the source is how that in between section is getting data from*/
        mMediatorRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                // it will be triggered when the source changes that means when the a request is made
                // So know we have an opportunity to do something before the data is sent back to the activity
                if (recipes != null){
                    mMediatorRecipes.setValue(recipes);
                    doneQuery(recipes);

                } else {
                    // Here the importance of cache database comes
                    // I need to search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery (List<Recipe> list){

        if (list != null){
            if (list.size() % 30 != 0 ){ // that means the query is exhausted that means there is no more search results for that query
                mIsQueryExhausted.setValue(true);
                return;
            }

        } else {
            mIsQueryExhausted.setValue(true);
            return;
        }

        if (!(oldListCount == list.size())){
            oldListCount = list.size();
        } else {
            mIsQueryExhausted.setValue(true);
            oldListCount = 0;
            return;
        }


    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    /**
     *
     * @return LiveData Recipe with a certain recipeId
     */
    public LiveData<Recipe> getRecipe(){
        return recipeApiClient.getRecipe();
    }

    /**
     * Method to get Recipes querying the recipes by calling Call<RecipeSearchResponse> getRecipe(ApiKey, query, pageNumber)
     * This method will be used in the {@link com.mustafa.foodapp.viewmodels.RecipeListViewModel }
     * @param query      the query that is the searched recipe
     * @param pageNumber the page number
     */
    public void searchRecipesApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        recipeApiClient.searchRecipeApi(query, pageNumber);
    }

    public void searchNextPage(){
        searchRecipesApi(mQuery, mPageNumber + 1);
    }

    public void cancelRequest(){
        recipeApiClient.cancelRequest();
    }

    /**
     * Query to get a recipe with a certain recipe id.
     * @param recipeId the recipe id
     */
    public void getRecipeById(String recipeId){
        recipeApiClient.getRecipeById(recipeId);
    }

    // For Recipe timeout
    public MutableLiveData<Boolean> isRecipeRequestTimedOut() {
        return recipeApiClient.isRecipeRequestTimedOut();
    }
}
