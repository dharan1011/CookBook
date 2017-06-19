package com.example.dharanaditya.cookbook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Dharan Aditya on 17-06-2017.
 */
@Parcel
public class Ingredient {
    @SerializedName("quantity")
    @Expose
    float quantity;

    @SerializedName("measure")
    @Expose
    String measure;

    @SerializedName("ingredient")
    @Expose
    String ingredient;

    public Ingredient() {
    }

    public Ingredient(float quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.ingredient = ingredient;
        this.measure = measure;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
