package com.mustafa.foodApp.viewmodels;


import android.app.Application;
import android.util.Log;

import com.mustafa.foodApp.models.Recipe;
import com.mustafa.foodApp.repositories.RecipeRepository;
import com.mustafa.foodApp.util.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


public class RecipeListViewModel extends AndroidViewModel {

    private static final String TAG = "RecipeListViewModel";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> viewState;

    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();

    private RecipeRepository recipeRepository;

    public static final String QUERY_EXHAUSTED_MSG = "No more results";


    // query extras

    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private int pageNumber;
    private String query;
    private boolean cancelRequest;
    private long requestStartTime;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = RecipeRepository.getInstance(application);

        init();

    }

    private void init() {
        if (viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewState() {
        return viewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return recipes;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setViewCategories() {
        viewState.setValue(ViewState.CATEGORIES);
    }

    public void searchRecipesApi(String query, int pageNumber) {

        if (!isPerformingQuery) {
            if (pageNumber == 0) {
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            this.query = query;
            isQueryExhausted = false;
            executeSearch();

        }

    }

    public void searchNextPage() {
        if (!isQueryExhausted && !isPerformingQuery) {
            pageNumber++;
            executeSearch();
        }

    }

    private void executeSearch() {
        cancelRequest = false;
        isPerformingQuery = true;
        viewState.setValue(ViewState.RECIPES);
        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepository.searchRecipesApi(query, pageNumber);

        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                // Before sending to the ui
                // Or before to set the value to the liveData i can react

                if (!cancelRequest) {
                    if (listResource != null) {
                        recipes.setValue(listResource);
                        if (listResource.status == Resource.Status.SUCCESS) {
                            isPerformingQuery = false;
                            if (listResource.data != null) {
                                if (listResource.data.size() == 0) {
                                    Log.d(TAG, "onChanged: Query is Exhausted...");
                                    recipes.setValue(new Resource<List<Recipe>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED_MSG)
                                    );
                                }
                            }
                            recipes.removeSource(repositorySource);
                        } else if (listResource.status == Resource.Status.ERROR) {
                            isPerformingQuery = false;
                            recipes.removeSource(repositorySource);
                        }

                    } else {
                        recipes.removeSource(repositorySource);
                    }
                } else {
                    recipes.removeSource(repositorySource);
                }


            }
        });
    }

    public void cancelSearchRequest() {
        if (isPerformingQuery){
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }

}















