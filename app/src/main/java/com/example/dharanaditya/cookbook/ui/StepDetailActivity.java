package com.example.dharanaditya.cookbook.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.provider.RecipeContract;
import com.example.dharanaditya.cookbook.ui.fragment.StepDetailFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.example.dharanaditya.cookbook.ui.StepsListActivity.RECIPE_ID;
import static com.example.dharanaditya.cookbook.ui.StepsListActivity.RECIPE_STEP_EXTRA;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnNextButtonClickListener {
    public static final String TAG = StepsListActivity.class.getSimpleName();
    private static final String STEP_STATE_KEY = "step_state_key";
    Step step;
    List<Step> stepList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
//        if (getIntent().hasExtra(RECIPE_STEP_EXTRA)) {

            step = Parcels.unwrap(getIntent().getParcelableExtra(RECIPE_STEP_EXTRA));

            //TODO load all step corresponding to recipe id
            if (savedInstanceState == null) {
                // TODO fetch all step corresponding to recipe id all
                stepList = getStepsWithRecipeId(Integer.toString(getIntent().getIntExtra(RECIPE_ID, 0)));

                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setCurrentStep(step);

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_steps_detail, stepDetailFragment)
                        .commit();
            } else {
                stepList = Parcels.unwrap(savedInstanceState.getParcelable(STEP_STATE_KEY));
            }

//        }
    }

    private List<Step> getStepsWithRecipeId(String intExtra) {
        // TODO
        List<Step> stepList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(RecipeContract.StepEntry.CONTENT_URI,
                null,
                RecipeContract.StepEntry.COLUMN_RECIPE_ID + "=?",
                new String[]{intExtra},
                null);

        if (cursor == null) return null;

        while (cursor.moveToNext()) {
            Step step = new Step(
                    cursor.getInt(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_ID)),
                    cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_VIDEO_URL)),
                    cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_THUMBNAIL_URL))
            );
            stepList.add(step);
        }
        return stepList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_STATE_KEY, Parcels.wrap(stepList));
    }

    @Override
    public void onNextButtonClick(int id) {
        if (id < stepList.size()) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setCurrentStep(stepList.get(id));

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_steps_detail, stepDetailFragment)
                    .commit();
        } else {
            Toast.makeText(this, "Ready To Eat", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
