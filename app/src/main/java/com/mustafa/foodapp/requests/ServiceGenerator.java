package com.mustafa.foodapp.requests;

import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mustafa.foodapp.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL);

    private static Retrofit retrofit = retrofitBuilder.build();

    //this will bring life to RecipeApi and provide the necessary code for the methods (requests) in RecipeApi
    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi(){
        return recipeApi;
    }
}
