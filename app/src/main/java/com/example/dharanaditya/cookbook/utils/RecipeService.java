package com.example.dharanaditya.cookbook.utils;

import com.example.dharanaditya.cookbook.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dhara on 17-06-2017.
 */

public interface RecipeService {
    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> fetchRecipes();
}
