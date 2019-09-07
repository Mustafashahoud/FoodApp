package com.mustafa.foodApp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mustafa.foodApp.BaseActivity;
import com.mustafa.foodApp.R;
import com.mustafa.foodApp.models.Recipe;
import com.mustafa.foodApp.util.Resource;
import com.mustafa.foodApp.viewmodels.RecipeViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    // UI components
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;

    private RecipeViewModel mRecipeViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            subscribeObserver(recipe.getRecipe_id());

        }
    }

    private void subscribeObserver (final String recipeId) {
        mRecipeViewModel.searchRecipeApi(recipeId).observe(this, new Observer<Resource<Recipe>>() {
            @Override
            public void onChanged(Resource<Recipe> recipeResource) {
                if (recipeResource != null){
                    if (recipeResource.data != null) {
                        switch (recipeResource.status) {
                            case LOADING:{
                                showProgressBar(true);
                                break;
                            }

                            case SUCCESS:{
                                showParent();
                                showProgressBar(false);
                                break;
                            }

                            case ERROR: {
                                showParent();
                                showProgressBar(false);
                                break;
                            }
                        }
                    }

                }
            }
        });
    }

    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}














