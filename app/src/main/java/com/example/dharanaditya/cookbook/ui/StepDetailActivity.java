package com.example.dharanaditya.cookbook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.ui.fragment.StepDetailFragment;

import org.parceler.Parcels;

import java.util.List;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnNextButtonClickListener {
    public static final String TAG = StepsListActivity.class.getSimpleName();
    List<Step> stepList;
    Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        if (getIntent().hasExtra("step_data")) {
            step = Parcels.unwrap(getIntent().getParcelableExtra("step_data"));
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setCurrentStep(step);
            Log.d(TAG, "onCreate: " + step.getDescription());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_steps_detail, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNextButtonClick(int id) {
        Log.d(TAG, "onNextButtonClick: " + id);
    }
}
