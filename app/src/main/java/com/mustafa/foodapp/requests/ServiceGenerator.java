package com.mustafa.foodApp.requests;


import com.mustafa.foodApp.util.Constants;
import com.mustafa.foodApp.util.LiveDataCallAdapter;
import com.mustafa.foodApp.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mustafa.foodApp.util.Constants.CONNECTION_TIMEOUT;
import static com.mustafa.foodApp.util.Constants.READ_TIMEOUT;
import static com.mustafa.foodApp.util.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()

            // time to establish connection to a server by default it is 10 seconds
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte read form the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            //time between each byte sent to the sever
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(false)

            .build();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory()) // For Converting Call To LiveData
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi(){
        return recipeApi;
    }
}
