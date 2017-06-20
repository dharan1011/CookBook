package com.example.dharanaditya.cookbook.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Ingredient;
import com.example.dharanaditya.cookbook.model.Recipe;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.provider.RecipeContract;
import com.example.dharanaditya.cookbook.ui.adapter.RecipeAdapter;
import com.example.dharanaditya.cookbook.utils.RecipeService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    //    Activity to show list of available recipe
    public static final String RECIPE_INDEX_EXTRA = "recipe_index_extra";
    public static final String RECIPE_NAME_EXTRA = "recipe_name_extra";
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1001;

    @BindView(R.id.rcv_recipe_list)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.pb_fetch_data)
    ProgressBar progressBar;
    RecipeAdapter recipeAdapter;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipeAdapter = new RecipeAdapter(this);

        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(
                new GridLayoutManager(this, (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 1 : 2)
        );
        // set recycler view adapter
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Query the database. If empty download the data a persist it locally in database
        if (getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI, null, null, null, null).getCount() == 0) {
            // show Progress bar and hide recyclerView
            showProgressBar();
            clearData();
            fetchData();
        } else
            // if data is already downloaded
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        recipeRecyclerView.setVisibility(View.INVISIBLE);
    }

    // fetch the data from the internet
    private void fetchData() {
        Retrofit builder = new Retrofit.Builder()
                .baseUrl(RecipeService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RecipeService recipeService = builder.create(RecipeService.class);
        Call<List<Recipe>> call = recipeService.fetchRecipes();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                // insert the data in local database
                insertRecipesIntoDatabase(response.body());

                // hide progress bar and show recycler view
                hideProgressBar();
                // Initialize loader to fetch data from database when data is inserted into database
                getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Failed to fetch content")
                        .setMessage("Please make sure you are connected to Internet")
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        }).show();
            }
        });
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        recipeRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*
    * Responds to recipe RecyclerView item clicks
    * @params position : position of view holder to load the steps and ingredients corresponding recipe id
    *
    * */
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, StepsListActivity.class);
        cursor.moveToPosition(position);
        int recipe_id = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
        i.putExtra(RECIPE_INDEX_EXTRA, recipe_id);
        i.putExtra(RECIPE_NAME_EXTRA, cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME)));
        updateWidget(recipe_id);
        startActivity(i);
    }

    private void updateWidget(int recipe_id) {
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(this).edit();
        preferences.putInt(RECIPE_INDEX_EXTRA, recipe_id);
        preferences.apply();
    }

    private void insertRecipesIntoDatabase(List<Recipe> body) {
        ContentValues[] recipeContentValue = new ContentValues[body.size()];
        for (int i = 0; i < body.size(); i++) {
            recipeContentValue[i] = new ContentValues();
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, body.get(i).getId());
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, body.get(i).getName());
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS, body.get(i).getServings());
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE, body.get(i).getImage());

            // insert recipe's step and ingredients into database
            insertIngredientsIntoDatabase(body.get(i).getIngredients(), body.get(i).getId());
            insertStepsIntoDatabase(body.get(i).getSteps(), body.get(i).getId());
        }
        getContentResolver().bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, recipeContentValue);

    }

    private void insertStepsIntoDatabase(List<Step> steps, int recipeId) {
        ContentValues[] stepContentValues = new ContentValues[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            stepContentValues[i] = new ContentValues();
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_RECIPE_ID, recipeId);
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_STEP_ID, steps.get(i).getId());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION, steps.get(i).getShortDescription());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_DESCRIPTION, steps.get(i).getDescription());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_VIDEO_URL, steps.get(i).getVideoURL());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_THUMBNAIL_URL, steps.get(i).getThumbnailURL());
        }
        getContentResolver().bulkInsert(RecipeContract.StepEntry.CONTENT_URI, stepContentValues);
    }

    private void insertIngredientsIntoDatabase(List<Ingredient> ingredients, int recipeId) {
        ContentValues[] ingredientContentValues = new ContentValues[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientContentValues[i] = new ContentValues();
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_RECIPE_ID, recipeId);
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_QUALITY, ingredients.get(i).getQuantity());
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_MEASURE, ingredients.get(i).getMeasure());
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT, ingredients.get(i).getIngredient());
        }
        getContentResolver().bulkInsert(RecipeContract.IngredientEntry.CONTENT_URI, ingredientContentValues);
    }

    // Used only for debug stage
    // clear the database before fetching data
    private void clearData() {
        getContentResolver().delete(RecipeContract.RecipeEntry.CONTENT_URI, null, null);
        getContentResolver().delete(RecipeContract.IngredientEntry.CONTENT_URI, null, null);
        getContentResolver().delete(RecipeContract.StepEntry.CONTENT_URI, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Loader to query the database about Recipe
        return new CursorLoader(this, RecipeContract.RecipeEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // temporary cursor to store queried data
        cursor = data;
        // set cursor to adapter
        recipeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipeAdapter.swapCursor(null);
    }
}
