package com.example.dharanaditya.cookbook.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.provider.RecipeContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dharan Aditya on 18-06-2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private OnRecipeItemClickListener onRecipeItemClickListener;
    private Cursor cursor;
    private Context context;

    public RecipeAdapter(Context context) {
        this.context = context;
        onRecipeItemClickListener = (OnRecipeItemClickListener) context;
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        if (this.cursor != null) notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            holder.bind(
                    cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS)));
            String url = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE));
//            String url = "https://d24pyncn3hxs0c.cloudfront.net/sites/default/files/styles/uc_product_full/public/Black-forest-cake-1-Kg-A.jpg?itok=IB1EbtO_";
            if (!url.isEmpty() && !url.equals(" "))
                holder.loadImage(url);

        }
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public interface OnRecipeItemClickListener {
        void onItemClick(int position);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imv_recipe_image)
        ImageView recipeImageView;
        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;
        @BindView(R.id.tv_servings_info)
        TextView servingInfoTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(String recipeName, int servingCount) {
            recipeNameTextView.setText(recipeName);
            servingInfoTextView.setText("Servings " + servingCount);
        }

        public void loadImage(String url) {
            recipeImageView.setBackground(null);
            Picasso.with(context)
                    .load(url)
                    .fit()
                    .into(recipeImageView);
        }

        @Override
        public void onClick(View v) {
            onRecipeItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
