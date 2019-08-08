package com.mustafa.foodapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.mustafa.foodapp.models.Recipe;

public abstract class ActivityRecipeBinding extends ViewDataBinding {
  @NonNull
  public final LinearLayout container;

  @NonNull
  public final LinearLayout ingredientsContainer;

  @NonNull
  public final ScrollView parentScrollView;

  @NonNull
  public final ImageView recipeImage;

  @NonNull
  public final TextView recipeSocialScore;

  @NonNull
  public final TextView recipeTitle;

  @Bindable
  protected Recipe mRecipe;

  protected ActivityRecipeBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, LinearLayout container, LinearLayout ingredientsContainer,
      ScrollView parentScrollView, ImageView recipeImage, TextView recipeSocialScore,
      TextView recipeTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.container = container;
    this.ingredientsContainer = ingredientsContainer;
    this.parentScrollView = parentScrollView;
    this.recipeImage = recipeImage;
    this.recipeSocialScore = recipeSocialScore;
    this.recipeTitle = recipeTitle;
  }

  public abstract void setRecipe(@Nullable Recipe recipe);

  @Nullable
  public Recipe getRecipe() {
    return mRecipe;
  }

  @NonNull
  public static ActivityRecipeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRecipeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRecipeBinding>inflate(inflater, com.mustafa.foodapp.R.layout.activity_recipe, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityRecipeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRecipeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRecipeBinding>inflate(inflater, com.mustafa.foodapp.R.layout.activity_recipe, null, false, component);
  }

  public static ActivityRecipeBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityRecipeBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityRecipeBinding)bind(component, view, com.mustafa.foodapp.R.layout.activity_recipe);
  }
}
