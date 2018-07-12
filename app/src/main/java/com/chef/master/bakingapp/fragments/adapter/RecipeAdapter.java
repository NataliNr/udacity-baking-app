package com.chef.master.bakingapp.fragments.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chef.master.bakingapp.Constants;
import com.chef.master.bakingapp.R;
import com.chef.master.bakingapp.api.model.BakingResult;
import com.chef.master.bakingapp.api.model.Ingredient;
import com.chef.master.bakingapp.room.local.IngredientContract;
import com.chef.master.bakingapp.widget.AppWidgetProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chef.master.bakingapp.Constants.SHAREDPREF;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private Context mContext;
    private List<BakingResult> recipes;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // Constructor
    public RecipeAdapter(Context c, List<BakingResult> recipes) {
        this.mInflater = LayoutInflater.from(c);
        mContext = c;
        this.recipes = recipes;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!recipes.get(position).getImage().isEmpty()) {
            Picasso.with(mContext).load(recipes.get(position).getImage())
//                .placeholder(R.drawable.ic_none).error(R.drawable.ic_error)
                    .into(holder.imageView);
        }
        BakingResult recipe=recipes.get(position);
        holder.name.setText(recipes.get(position).getName());
        holder.servings.setText(recipes.get(position).getServings().toString());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name_recipe)
        TextView name;
        @BindView(R.id.img_recipe)
        ImageView imageView;
        @BindView(R.id.servings_recipe)
        TextView servings;

      private   ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    BakingResult getItem(int id) {
        return recipes.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}