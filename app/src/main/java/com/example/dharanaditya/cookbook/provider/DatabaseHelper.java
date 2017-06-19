package com.example.dharanaditya.cookbook.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dharan Aditya on 18-06-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "recipe.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE + " TEXT" +
                " );";
        final String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + RecipeContract.IngredientEntry.TABLE_NAME + " (" +
                RecipeContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeContract.IngredientEntry.COLUMN_MEASURE + " TEXT, " +
                RecipeContract.IngredientEntry.COLUMN_QUALITY + " FLOAT, " +
                RecipeContract.IngredientEntry.COLUMN_INGREDIENT + " TEXT" +
                " )";
        final String CREATE_STEP_TABLE = "CREATE TABLE " + RecipeContract.StepEntry.TABLE_NAME + " (" +
                RecipeContract.StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.StepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeContract.StepEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, " +
                RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION + " TEXT," +
                RecipeContract.StepEntry.COLUMN_DESCRIPTION + " TEXT," +
                RecipeContract.StepEntry.COLUMN_VIDEO_URL + " TEXT," +
                RecipeContract.StepEntry.COLUMN_THUMBNAIL_URL + " TEXT" +
                " );";
        db.execSQL(CREATE_RECIPE_TABLE);
        db.execSQL(CREATE_INGREDIENT_TABLE);
        db.execSQL(CREATE_STEP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.StepEntry.TABLE_NAME);
        onCreate(db);
    }
}
