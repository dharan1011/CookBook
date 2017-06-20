package com.example.dharanaditya.cookbook.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.provider.RecipeContract;
import com.example.dharanaditya.cookbook.ui.StepsListActivity;
import com.example.dharanaditya.cookbook.ui.adapter.IngredientAdapter;
import com.example.dharanaditya.cookbook.ui.adapter.StepAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = StepListFragment.class.getSimpleName();
    private static final int STEP_LOADER_ID = 2002;
    private static final int INGREDIENTS_LOADER_ID = 2003;

    @BindView(R.id.rcv_step_list)
    RecyclerView stepRecyclerView;

    @BindView(R.id.rcv_ingredient_list)
    RecyclerView ingredientRecyclerView;

    private OnStepClickListener mListener;
    private StepAdapter stepAdapter;

    private IngredientAdapter ingredientAdapter;

    private String recipeId;

    public StepListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        stepAdapter = new StepAdapter(context);
        recipeId = getArguments().getString(StepsListActivity.RECIPE_ID);
        ingredientAdapter = new IngredientAdapter(context);

        if (context instanceof OnStepClickListener) {

            mListener = (OnStepClickListener) context;
            stepAdapter.setStepClickListener(mListener);

            Log.d(TAG, "onAttach: " + recipeId);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(STEP_LOADER_ID, null, this);
        getLoaderManager().initLoader(INGREDIENTS_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, v);
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stepRecyclerView.setAdapter(stepAdapter);

        ingredientRecyclerView.setHasFixedSize(true);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case STEP_LOADER_ID:
                return new CursorLoader(getContext(), RecipeContract.StepEntry.CONTENT_URI,
                null,
                RecipeContract.StepEntry.COLUMN_RECIPE_ID + "=?",
                new String[]{recipeId},
                null);
            case INGREDIENTS_LOADER_ID:
                return new CursorLoader(getContext(), RecipeContract.IngredientEntry.CONTENT_URI,
                        null,
                        RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + "=?",
                        new String[]{recipeId},
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case STEP_LOADER_ID:
                stepAdapter.swapCursor(data);
                break;
            case INGREDIENTS_LOADER_ID:
                ingredientAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case STEP_LOADER_ID:
                stepAdapter.swapCursor(null);
                break;
            case INGREDIENTS_LOADER_ID:
                ingredientAdapter.swapCursor(null);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnStepClickListener {
        void onStepClick(Step step);
    }
}
