package com.example.dharanaditya.cookbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.ui.fragment.StepDetailFragment;
import com.example.dharanaditya.cookbook.ui.fragment.StepListFragment;

import org.parceler.Parcels;

public class StepsListActivity extends AppCompatActivity implements StepListFragment.OnStepClickListener {
    public static final String TAG = StepsListActivity.class.getSimpleName();

    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_STEP_EXTRA = "recipe_step_extra";
    private static final java.lang.String RECIPE_ID_STATE_KEY = "recipe_id_state_key";
    public static boolean isTwoPane;
    int recipe_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);

        getSupportActionBar().setTitle(getIntent().getStringExtra(MainActivity.RECIPE_NAME_EXTRA));

        isTwoPane = (findViewById(R.id.two_pane_ref) != null);

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

                if (isTwoPane) {
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();

                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container_steps_detail, stepDetailFragment)
                            .commit();
                }
            }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID_STATE_KEY, recipe_id);
    }

    // Starts StepDetailActivity by passing in current step object
    @Override
    public void onStepClick(Step step) {
        if (!isTwoPane) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_STEP_EXTRA, Parcels.wrap(step));
        bundle.putInt(RECIPE_ID, recipe_id);
        intent.putExtras(bundle);
        startActivity(intent);
        } else {

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setCurrentStep(step);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_steps_detail, stepDetailFragment)
                    .commit();
        }
    }

}
