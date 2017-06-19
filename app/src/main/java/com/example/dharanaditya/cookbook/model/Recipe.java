package com.example.dharanaditya.cookbook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Dharan Aditya on 17-06-2017.
 */
@Parcel
public class Recipe {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("ingredients")
    @Expose
    List<Ingredient> ingredients = null;

    @SerializedName("steps")
    @Expose
    List<Step> steps = null;

    @SerializedName("servings")
    @Expose
    int servings;

    @SerializedName("image")
    @Expose
    String image;

    public Recipe() {
    }

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}

