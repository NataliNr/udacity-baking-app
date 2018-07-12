package com.chef.master.bakingapp.fragments.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chef.master.bakingapp.R;
import com.chef.master.bakingapp.api.model.BakingResult;
import com.chef.master.bakingapp.api.model.Ingredient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private Context mContext;
    private List<Ingredient> uriImage;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // Constructor
    public IngredientAdapter(Context c, List<Ingredient> uriImage) {
        this.mInflater = LayoutInflater.from(c);
        mContext = c;
        this.uriImage = uriImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutForIngredients = R.layout.item_ingredient;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layoutForIngredients, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(uriImage.get(position).getIngredient());
        if (!uriImage.get(position).getMeasure().isEmpty()) {
            holder.measure.setText(uriImage.get(position).getMeasure());
        }
        holder.quantity.setText(Float.toString(uriImage.get(position).getQuantity()));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return uriImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ingredient_quantity)
        TextView quantity;
        @BindView(R.id.ingredient_measure)
        TextView measure;
        @BindView(R.id.ingredient_name)
        TextView name;

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
    Ingredient getItem(int id) {
        return uriImage.get(id);
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