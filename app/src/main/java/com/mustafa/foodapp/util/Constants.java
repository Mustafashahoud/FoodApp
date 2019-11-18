package com.mustafa.foodapp.util;

public class Constants {
    //www.food2fork.com is shut down 
    //public static final String BASE_URL = "https://www.food2fork.com";
    
    public static final String BASE_URL = "https://recipesapi.herokuapp.com";
    //public static final String API_KEY = "ef915f88aa2cdf50853476a98e4b97e6";
    public static final String API_KEY = "";
    public static final int NETWORK_TIMEOUT = 5000;

    public static final String[] DEFAULT_SEARCH_CATEGORIES_NAMES =
            {"Barbeque", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
                    "barbeque",
                    "breakfast",
                    "chicken",
                    "beef",
                    "brunch",
                    "dinner",
                    "wine",
                    "italian"
            };
}
