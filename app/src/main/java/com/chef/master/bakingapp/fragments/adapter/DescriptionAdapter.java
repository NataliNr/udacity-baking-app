package com.chef.master.bakingapp.fragments.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chef.master.bakingapp.R;
import com.chef.master.bakingapp.api.model.Ingredient;
import com.chef.master.bakingapp.api.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.ViewHolder> {
    private Context mContext;
    private List<Step> uriImage;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // Constructor
    public DescriptionAdapter(Context c, List<Step> uriImage) {
        this.mInflater = LayoutInflater.from(c);
        mContext = c;
        this.uriImage = uriImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutForDescription = R.layout.item_description;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layoutForDescription, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.description.setText(uriImage.get(position).getShortDescription());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return uriImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.description)
        TextView description;

        ViewHolder(View itemView) {
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
    Step getItem(int id) {
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