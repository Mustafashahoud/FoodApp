package com.mustafa.foodapp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mustafa.foodapp.R;
import com.mustafa.foodapp.databinding.RecipeItemBinding;
import com.mustafa.foodapp.models.Recipe;
import com.mustafa.foodapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecipesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // For View Type since we have two ViewHolder we wanna use them in the same RecyclerViewAdapter
    private final static int RECIPE_TYPE = 1;
    private final static int LOADING_TYPE = 2;
    private final static int CATEGORY_TYPE = 3;
    private final static int Exhausted_TYPE = 4;

    private List<Recipe> mRecipes = new ArrayList<>();
    private OnRecipeClickListener onRecipeClickListener;


    public RecipesRvAdapter(OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view;
        switch (viewType) {
            //Case List recipes
            case RECIPE_TYPE: {
                RecipeItemBinding mRecipeItemBinding = RecipeItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
                return new MyViewHolder(mRecipeItemBinding, onRecipeClickListener);
            }
            //Case Loading layout
            case LOADING_TYPE: {
               /* LayoutLoadingListItemBinding mLayoutLoadingListItemBinding = LayoutLoadingListItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
                return new LoadingViewHolder(mLayoutLoadingListItemBinding);*/

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            }

            //Case Loading layout
            case CATEGORY_TYPE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, onRecipeClickListener);
            }

            //Case Loading layout
            case Exhausted_TYPE: {
               /* LayoutLoadingListItemBinding mLayoutLoadingListItemBinding = LayoutLoadingListItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
                return new LoadingViewHolder(mLayoutLoadingListItemBinding);*/

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_search_exhausted, parent, false);
                return new SearchExhaustedViewHolder(view);
            }

            default: {
                RecipeItemBinding mRecipeItemBinding = RecipeItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
                return new MyViewHolder(mRecipeItemBinding, onRecipeClickListener);
            }
        }

        // 1# way
     /*   View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);*/

        /*return new MyViewHolder(mRecipeItemBinding);*/

        //2# way
     /*   RecipeItemBinding mRecipeItemBinding = RecipeItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);*/

//        return new MyViewHolder(mRecipeItemBinding, onRecipeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == RECIPE_TYPE) {
            //ViewModel is holding all the liveData
            ((MyViewHolder) holder).mRecipeItemBinding.setRecipe(mRecipes.get(position));
        } else if (viewType == CATEGORY_TYPE) {
            Uri path = Uri.parse("android.resource://com.mustafa.foodapp/drawable/" + mRecipes.get(position).getImage_url());
            Glide.with(holder.itemView.getContext())
                    .load(path)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(((CategoryViewHolder) holder).categoryImage);

            ((CategoryViewHolder) holder).categoryTile.setText(mRecipes.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*Here we write any logic we wanna to get the type and we will need later on
        to set that logic using a method to be used inside the activity*/
        if (mRecipes.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        }
        else if (mRecipes.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        }
        else if (position == mRecipes.size() -1
                 && position !=0
                 && !mRecipes.get(position).getTitle().equals("EXHAUSTED...") ) {
            return LOADING_TYPE;
        }
        else if (mRecipes.get(position).getTitle().equals("EXHAUSTED...")) {
            return Exhausted_TYPE;
        }
        else {
            return RECIPE_TYPE;
        }
    }

    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        mRecipes.add(exhaustedRecipe);
//        mRecipes.get(mRecipes.size() - 1 ).setTitle("EXHAUSTED...");
        notifyDataSetChanged();
    }

    private void hideLoading(){
        if (isLoading()){
            for(Recipe recipe : mRecipes){
                if (recipe != null) {
                    if (recipe.getTitle().equals("LOADING...")){
                        mRecipes.remove(recipe);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            mRecipes = loadingList;
            notifyDataSetChanged();
        }
    }

    public void displaySearchCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES_NAMES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES_NAMES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        mRecipes = categories;
        notifyDataSetChanged();
    }

    private boolean isLoading() {
        if (mRecipes.size() > 0) {
            return mRecipes.get(mRecipes.size() - 1).getTitle().equals("LOADING...");
        }
        return false;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    public Recipe getCurrentRecipe(int position){
       if (mRecipes.size() > 0){
           return mRecipes.get(position);
       }
       return null;
    }


    /***View Holders***/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnRecipeClickListener onRecipeClickListener;
        RecipeItemBinding mRecipeItemBinding;

        public MyViewHolder(@NonNull RecipeItemBinding recipeItemBinding, OnRecipeClickListener onRecipeClickListener) {
            super(recipeItemBinding.getRoot());
            this.onRecipeClickListener = onRecipeClickListener;
            //1# way
            // mRecipeItemBinding = RecipeItemBinding.bind(itemView);

            //2# way
            this.mRecipeItemBinding = recipeItemBinding;

            mRecipeItemBinding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onRecipeClickListener != null) {
                onRecipeClickListener.onRecipeClick(getAdapterPosition());
            }
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public class SearchExhaustedViewHolder extends RecyclerView.ViewHolder {
        public SearchExhaustedViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnRecipeClickListener listener;
        CircleImageView categoryImage;
        TextView categoryTile;

        public CategoryViewHolder(@NonNull View itemView, OnRecipeClickListener listener) {
            super(itemView);
            this.listener = listener;
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryTile = itemView.findViewById(R.id.category_title);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onCategoryClick(categoryTile.getText().toString());
            }
        }
    }
}
