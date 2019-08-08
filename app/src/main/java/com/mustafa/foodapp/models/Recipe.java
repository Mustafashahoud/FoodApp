package com.mustafa.foodapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {
    private String publisher;
    private List<String> ingredients;
    private String recipe_id;

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String image_url;


    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    private float social_rank;

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public List<String> getIngredients() {
        return ingredients;
    }

    public Recipe(String publisher, List<String> ingredients, String recipe_id, String image_url, int social_rank, String title) {
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.recipe_id = recipe_id;
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.title = title;
    }
    public Recipe(){

    }

    protected Recipe(Parcel in) {
        publisher = in.readString();
        ingredients = in.createStringArrayList();
        recipe_id = in.readString();
        image_url = in.readString();
        social_rank = in.readFloat();
        title = in.readString();
    }

    public void setSocial_rank(float social_rank) {
        this.social_rank = social_rank;
    }
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getPublisher() {
        return publisher;
    }





    public String getRecipe_id() {
        return recipe_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public float getSocial_rank() {
        return social_rank;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "publisher='" + publisher + '\'' +
                ", ingredients=" + ingredients +
                ", recipe_id='" + recipe_id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", social_rank=" + social_rank +
                ", title='" + title + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(publisher);
        dest.writeStringList(ingredients);
        dest.writeString(recipe_id);
        dest.writeString(image_url);
        dest.writeFloat(social_rank);
        dest.writeString(title);
    }


}
