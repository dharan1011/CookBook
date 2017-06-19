package com.example.dharanaditya.cookbook.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.provider.RecipeContract;
import com.example.dharanaditya.cookbook.ui.StepDetailActivity;
import com.example.dharanaditya.cookbook.ui.fragment.StepListFragment;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dharan Aditya on 18-06-2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    public static final String TAG = StepAdapter.class.getSimpleName();
    private Context context;
    private StepListFragment.OnStepClickListener stepClickListener;
    private Cursor cursor;

    public StepAdapter(Context context) {
        this.context = context;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position) {
        if (cursor.moveToPosition(position)) {
            holder.bind(cursor.getInt(
                    cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_ID)),
                    cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION)));
        }
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        if (this.cursor != null) notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void setStepClickListener(StepListFragment.OnStepClickListener stepClickListener) {
        this.stepClickListener = stepClickListener;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_step_id)
        TextView idTextView;
        @BindView(R.id.tv_step_short_description)
        TextView shortDescriptionTextView;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int id, String shortDescription) {
            idTextView.setText(Integer.toString(id + 1));
            shortDescriptionTextView.setText(shortDescription);
        }

        @Override
        public void onClick(View v) {
            if (cursor.moveToPosition(getAdapterPosition())) {
                Step step = new Step(
                        cursor.getInt(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_ID)),
                        cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_VIDEO_URL)),
                        cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_THUMBNAIL_URL))
                );
                stepClickListener.onStepClick(step);
            }
        }
    }
}
