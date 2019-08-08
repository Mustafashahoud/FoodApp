package com.mustafa.foodapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mustafa.foodapp.R;
import com.mustafa.foodapp.databinding.ActivityRecipeBinding;
import com.mustafa.foodapp.models.Recipe;
import com.mustafa.foodapp.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class RecipeActivity extends BaseActivity {
    private ActivityRecipeBinding mBinding;
    private static final String RECIPE_INTENT_KEY = "recipe key";
    private ScrollView mScrollView;
    private RecipeViewModel recipeViewModel;
    private static final String TAG = "RecipeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_recipe, null, false);
        mScrollView = mBinding.parentScrollView;
        setContentView(mBinding.getRoot());

        // This method is implemented in the BaseActivity.
        showProgressBar(true);

        SubscribeObservers();

        recipeViewModel.getRecipeById(getIncomingIntentRecipeId());
    }

    private void SubscribeObservers(){
        recipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null){
                    if (recipe.getRecipe_id().equals(recipeViewModel.getRecipeId())){
                        mBinding.setRecipe(recipe);
                        mScrollView.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                        recipeViewModel.setDidRetrieveRecipe(true);
                    }
                }
            }
        });

        recipeViewModel.isRecipeRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean && !recipeViewModel.isDidRetrieveRecipe()){
                    // aBoolean is true that means we've got a timeout request.
                    Log.d(TAG, "onChanged: " + "Recipe timed out ...");
                    mBinding.setRecipe(getErrorRecipe());
                    showProgressBar(false);
                }
            }
        });
    }

    private String getIncomingIntentRecipeId(){
        if (getIntent().hasExtra(RECIPE_INTENT_KEY)){
            String recipe_id = getIntent().getStringExtra(RECIPE_INTENT_KEY);
            return recipe_id;
        }
        return null;
    }

    private Recipe getErrorRecipe(){
        Recipe recipe = new Recipe();
        recipe.setTitle("Error retrieving recipe...");
        recipe.setSocial_rank(0);
        List<String> list = new ArrayList<>();
        list.add("Error");
        recipe.setIngredients(list);
        return recipe;
    }
}

