package com.example.dharanaditya.cookbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Recipe;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.ui.fragment.StepListFragment;

import org.parceler.Parcels;

public class StepsListActivity extends AppCompatActivity implements StepListFragment.OnStepClickListener {
    public static final String TAG = StepsListActivity.class.getSimpleName();
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_STEP_EXTRA = "recipe_step_extra";
    private static final java.lang.String RECIPE_ID_STATE_KEY = "recipe_id_state_key";
    int recipe_id;
    private Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        if (getIntent().hasExtra(MainActivity.RECIPE_INDEX_EXTRA)) {

            if (savedInstanceState == null)
                recipe_id = getIntent().getIntExtra(MainActivity.RECIPE_INDEX_EXTRA, 0);
            else
                recipe_id = savedInstanceState.getInt(RECIPE_ID_STATE_KEY);

            Bundle bundle = new Bundle();
            bundle.putString(RECIPE_ID, Integer.toString(recipe_id));

            if (savedInstanceState == null) {
                StepListFragment stepListFragment = new StepListFragment();
                stepListFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_steps_list, stepListFragment)
                        .commit();
            }
        } else {
            // TODO handle error case
            Log.d(TAG, "onCreate: Unable to get Parcelable Extra");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID_STATE_KEY, recipe_id);
    }

    @Override
    public void onStepClick(Step step) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_STEP_EXTRA, Parcels.wrap(step));
        bundle.putInt(RECIPE_ID, recipe_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
