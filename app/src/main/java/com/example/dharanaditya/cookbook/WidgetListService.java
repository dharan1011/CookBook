package com.example.dharanaditya.cookbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dharanaditya.cookbook.provider.RecipeContract;
import com.example.dharanaditya.cookbook.ui.MainActivity;

/**
 * Created by Dharan Aditya on 21-06-2017.
 */

public class WidgetListService extends RemoteViewsService {
    public static final String TAG = WidgetListService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListAdapter(getApplicationContext());
    }

    public class WidgetListAdapter implements RemoteViewsService.RemoteViewsFactory, SharedPreferences.OnSharedPreferenceChangeListener {
        private Context context;
        private int recipeId = 1;
        private Cursor cursor;

        public WidgetListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            cursor = getContentResolver().query(RecipeContract.IngredientEntry.CONTENT_URI,
                    null,
                    RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + "=?",
                    new String[]{Integer.toString(recipeId)},
                    RecipeContract.IngredientEntry._ID);
        }

        @Override
        public void onDataSetChanged() {
            if (recipeId != 0) {
                cursor = getContentResolver().query(RecipeContract.IngredientEntry.CONTENT_URI,
                        null,
                        RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + "=?",
                        new String[]{Integer.toString(recipeId)},
                        RecipeContract.IngredientEntry._ID);
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return (cursor != null) ? cursor.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (cursor.moveToPosition(position)) {
                Log.d(TAG, "getViewAt: True");
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.item_ingredient);
                views.setTextViewText(R.id.tv_ingredient_name, cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT)));
                views.setTextViewText(R.id.tv_ingredient_measure, cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_MEASURE)));
                views.setTextViewText(R.id.tv_ingredient_quantity, cursor.getInt(cursor.getInt(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_QUALITY))) + "");

                return views;
            }
            Log.d(TAG, "getViewAt: False");
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            recipeId = sharedPreferences.getInt(MainActivity.RECIPE_INDEX_EXTRA, 0);
            onDataSetChanged();
        }
    }
}
