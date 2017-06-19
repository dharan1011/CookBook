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
import com.example.dharanaditya.cookbook.ui.adapter.StepAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = StepListFragment.class.getSimpleName();
    private static int LOADER_ID = 2002;
    @BindView(R.id.rcv_step_list)
    RecyclerView stepRecyclerView;
    private OnStepClickListener mListener;
    private StepAdapter stepAdapter;
    private String recipeId;

    public StepListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            mListener = (OnStepClickListener) context;

            stepAdapter = new StepAdapter(context);
            stepAdapter.setStepClickListener(mListener);

            recipeId = getArguments().getString(StepsListActivity.RECIPE_ID);

            Log.d(TAG, "onAttach: " + recipeId);
        } else {
            // TODO handle error case
            throw new RuntimeException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), RecipeContract.StepEntry.CONTENT_URI,
                null,
                RecipeContract.StepEntry.COLUMN_RECIPE_ID + "=?",
                new String[]{recipeId},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        stepAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stepAdapter.swapCursor(null);
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
