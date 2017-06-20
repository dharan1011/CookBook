package com.example.dharanaditya.cookbook.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.provider.RecipeContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dharan Aditya on 21-06-2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private Cursor cursor;
    private Context context;

    public IngredientAdapter(Context context) {
        this.context = context;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(v);
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        if (this.cursor != null) notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            holder.bind(
                    cursor.getFloat(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_QUALITY)),
                    cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_MEASURE)),
                    cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT))
            );
        }
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_name)
        TextView ingredientTextView;
        @BindView(R.id.tv_ingredient_measure)
        TextView measureTextView;
        @BindView(R.id.tv_ingredient_quantity)
        TextView quantityTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(float quantity, String measure, String ingredient) {
            ingredientTextView.setText(ingredient);
            measureTextView.setText(measure);
            quantityTextView.setText(Float.toString(quantity));
        }
    }
}
