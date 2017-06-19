package com.example.dharanaditya.cookbook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Recipe;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.ui.fragment.StepListFragment;

import org.parceler.Parcels;

public class StepsListActivity extends AppCompatActivity implements StepListFragment.OnStepClickListener {
    public static final String TAG = StepsListActivity.class.getSimpleName();
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        if (getIntent().hasExtra("recipe_id")) {
            Bundle bundle = new Bundle();
            bundle.putString("recipe_id", Integer.toString(getIntent().getIntExtra("recipe_id", 0)));
            if (savedInstanceState == null) {
                StepListFragment stepListFragment = new StepListFragment();
                stepListFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_steps_list, stepListFragment)
                        .commit();
            }
        } else {
            Log.d(TAG, "onCreate: Unable to get Parcelable Extra");
        }
    }

    @Override
    public void onStepClick(Step step) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("step_data", Parcels.wrap(step));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
