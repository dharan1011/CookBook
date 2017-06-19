package com.example.dharanaditya.cookbook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class RecipeContentProvider extends android.content.ContentProvider {
    private static final int RECIPE = 100;
    private static final int RECIPE_WITH_ID = 101;
    private static final int INGREDIENT = 200;
    private static final int INGREDIENT_WITH_ID = 201;
    private static final int STEP = 300;
    private static final int STEP_WITH_ID = 301;
    private UriMatcher uriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.RECIPE_PATH, RECIPE);
//        uriMatcher.addURI(RecipeContract.AUTHORITY,RecipeContract.RECIPE_PATH+"/#",RECIPE_WITH_ID);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.INGREDIENT_PATH, INGREDIENT);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.INGREDIENT_PATH + "/#", INGREDIENT_WITH_ID);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.STEP_PATH, STEP);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.STEP_PATH + "/#", STEP_WITH_ID);
        return uriMatcher;
    }

    DatabaseHelper databaseHelper;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRow = 0;
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case RECIPE:
                deletedRow = db.delete(RecipeContract.RecipeEntry.TABLE_NAME, null, null);
                break;
            case INGREDIENT:
                deletedRow = db.delete(RecipeContract.IngredientEntry.TABLE_NAME, null, null);
                break;
            case STEP:
                deletedRow = db.delete(RecipeContract.StepEntry.TABLE_NAME, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (deletedRow > 0) getContext().getContentResolver().notifyChange(uri, null);

        return deletedRow;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int rowsInserted = 0;
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case RECIPE:
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, cv);
                        if (id != -1) rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case INGREDIENT:
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long id = db.insert(RecipeContract.IngredientEntry.TABLE_NAME, null, cv);
                        if (id != -1) rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case STEP:
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long id = db.insert(RecipeContract.StepEntry.TABLE_NAME, null, cv);
                        if (id != -1) rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (rowsInserted > 0) getContext().getContentResolver().notifyChange(uri, null);
        return rowsInserted;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case RECIPE:
                cursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID);
                break;
            case STEP:
                cursor = db.query(RecipeContract.StepEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        RecipeContract.StepEntry.COLUMN_STEP_ID);
                break;
            case INGREDIENT:
                cursor = db.query(RecipeContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        RecipeContract.IngredientEntry._ID);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (cursor != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
