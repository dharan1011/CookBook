package com.example.dharanaditya.cookbook.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dharan Aditya on 18-06-2017.
 */

public class RecipeContract {
    private RecipeContract() {
    }

    public static final String AUTHORITY = "com.example.dharanaditya.cookbook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String RECIPE_PATH = "recipe";
    public static final String INGREDIENT_PATH = "ingredient";
    public static final String STEP_PATH = "step";

    public static class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipes";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_RECIPE_SERVINGS = "servings";
        public static final String COLUMN_RECIPE_IMAGE = "image";
    }

    public static class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredients";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(INGREDIENT_PATH).build();

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_QUALITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";

    }

    public static class StepEntry implements BaseColumns {
        public static final String TABLE_NAME = "steps";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(STEP_PATH).build();

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "video_url";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    }

}