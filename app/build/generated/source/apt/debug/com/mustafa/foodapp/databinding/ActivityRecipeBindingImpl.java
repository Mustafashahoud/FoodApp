package com.mustafa.foodapp.databinding;
import com.mustafa.foodapp.R;
import com.mustafa.foodapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityRecipeBindingImpl extends ActivityRecipeBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.container, 5);
        sViewsWithIds.put(R.id.ingredients_container, 6);
    }
    // views
    @NonNull
    private final android.widget.TextView mboundView4;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityRecipeBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds));
    }
    private ActivityRecipeBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.ScrollView) bindings[0]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[2]
            );
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.parentScrollView.setTag(null);
        this.recipeImage.setTag(null);
        this.recipeSocialScore.setTag(null);
        this.recipeTitle.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.recipe == variableId) {
            setRecipe((com.mustafa.foodapp.models.Recipe) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setRecipe(@Nullable com.mustafa.foodapp.models.Recipe Recipe) {
        this.mRecipe = Recipe;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.recipe);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        int mathRoundRecipeSocialRank = 0;
        java.lang.String recipeImageUrl = null;
        java.util.List<java.lang.String> recipeIngredients = null;
        float recipeSocialRank = 0f;
        java.lang.String stringValueOfMathRoundRecipeSocialRank = null;
        java.lang.String stringUtilsGetStringIngredientsRecipeIngredients = null;
        com.mustafa.foodapp.models.Recipe recipe = mRecipe;
        java.lang.String RecipeTitle1 = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (recipe != null) {
                    // read recipe.image_url
                    recipeImageUrl = recipe.getImage_url();
                    // read recipe.ingredients
                    recipeIngredients = recipe.getIngredients();
                    // read recipe.social_rank
                    recipeSocialRank = recipe.getSocial_rank();
                    // read recipe.title
                    RecipeTitle1 = recipe.getTitle();
                }


                // read StringUtils.getStringIngredients(recipe.ingredients)
                stringUtilsGetStringIngredientsRecipeIngredients = com.mustafa.foodapp.util.StringUtils.getStringIngredients(recipeIngredients);
                // read Math.round(recipe.social_rank)
                mathRoundRecipeSocialRank = java.lang.Math.round(recipeSocialRank);


                // read String.valueOf(Math.round(recipe.social_rank))
                stringValueOfMathRoundRecipeSocialRank = java.lang.String.valueOf(mathRoundRecipeSocialRank);
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView4, stringUtilsGetStringIngredientsRecipeIngredients);
            com.mustafa.foodapp.util.CustomGlideModule.loadImage(this.recipeImage, recipeImageUrl);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.recipeSocialScore, stringValueOfMathRoundRecipeSocialRank);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.recipeTitle, RecipeTitle1);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): recipe
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}