package com.mustafa.foodapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.mustafa.foodapp.models.Recipe;

public abstract class RecipeItemBinding extends ViewDataBinding {
  @NonNull
  public final TextView publisherTv;

  @NonNull
  public final TextView rating;

  @NonNull
  public final ImageView recipeImage;

  @NonNull
  public final RelativeLayout relativeLayout;

  @NonNull
  public final TextView titleTv;

  @Bindable
  protected Recipe mRecipe;

  protected RecipeItemBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView publisherTv, TextView rating, ImageView recipeImage,
      RelativeLayout relativeLayout, TextView titleTv) {
    super(_bindingComponent, _root, _localFieldCount);
    this.publisherTv = publisherTv;
    this.rating = rating;
    this.recipeImage = recipeImage;
    this.relativeLayout = relativeLayout;
    this.titleTv = titleTv;
  }

  public abstract void setRecipe(@Nullable Recipe recipe);

  @Nullable
  public Recipe getRecipe() {
    return mRecipe;
  }

  @NonNull
  public static RecipeItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static RecipeItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<RecipeItemBinding>inflate(inflater, com.mustafa.foodapp.R.layout.recipe_item, root, attachToRoot, component);
  }

  @NonNull
  public static RecipeItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static RecipeItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<RecipeItemBinding>inflate(inflater, com.mustafa.foodapp.R.layout.recipe_item, null, false, component);
  }

  public static RecipeItemBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static RecipeItemBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (RecipeItemBinding)bind(component, view, com.mustafa.foodapp.R.layout.recipe_item);
  }
}
