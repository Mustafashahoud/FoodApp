package com.mustafa.foodapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.mustafa.foodapp.R;
import com.mustafa.foodapp.adapters.OnRecipeClickListener;
import com.mustafa.foodapp.adapters.RecipesRvAdapter;
import com.mustafa.foodapp.adapters.VerticalSpacingItemDecorator;
import com.mustafa.foodapp.models.Recipe;
import com.mustafa.foodapp.viewmodels.RecipeListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeListActivity extends BaseActivity implements OnRecipeClickListener {

    private static final String TAG = "RecipeListActivity";
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecipesRvAdapter mRecipesRvAdapter;
    private MenuItem searchMenuItem;
    private String defaultQuery = "Chicken breast";
    private SearchView searchView;
    private List<Recipe> mRecipes = new ArrayList<>();
    private static final String RECIPE_INTENT_KEY = "recipe key";

    private RecipeListViewModel mRecipeListViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initViews();
        initToolbar();
        initSearchView();
        initRecyclerView();
        if (!mRecipeListViewModel.isViewingRecipes()){
            //Display search category
            displayCategory();
        }
        subscribeObserver();
    }

    private void subscribeObserver() {
        mRecipeListViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null){
                if (mRecipeListViewModel.isViewingRecipes()){
                    for(Recipe recipe : recipes){
//                        Log.d(TAG, "onChanged: " + recipe.getTitle());
                        Log.d(TAG, "printRecipes: " + recipe.getRecipe_id() + ", " + recipe.getTitle());

                    }
                    mRecipeListViewModel.setPerformingQuery(false);
                    mRecipesRvAdapter.setRecipes(recipes);
                    mRecipes = recipes;
                }
            }
        });

        mRecipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    mRecipesRvAdapter.setQueryExhausted();
                }

            }
        });
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView(){
        mRecipesRvAdapter = new RecipesRvAdapter( this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipesRvAdapter);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(verticalSpacingItemDecorator);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)){ /*That means the recyclerView can no longer scroll vertically*/
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initViews(){
        toolbar = findViewById(R.id.activity_main_toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    private void initSearchView(){
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d(TAG, "onQueryTextSubmit: " + "QUERY:" + query);
                mRecipesRvAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(query, 1);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_INTENT_KEY, mRecipesRvAdapter.getCurrentRecipe(position).getRecipe_id());
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mRecipesRvAdapter.displayLoading();
        mRecipeListViewModel.searchRecipesApi(category, 1);
        searchView.clearFocus();
    }

    private void displayCategory(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mRecipesRvAdapter.displaySearchCategories();

    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        } else {
            displayCategory();
        }
    }


    /*****************For The Menu************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_categories){
            displayCategory();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(searchListener);
        return true;
    }
    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchMenuItem.collapseActionView();
            defaultQuery = query;
            showProgressBar(false);
            mRecipeListViewModel.searchRecipesApi(query, 1);
            showProgressBar(false);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };*/
}
