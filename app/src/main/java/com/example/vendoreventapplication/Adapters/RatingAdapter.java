package com.example.vendoreventapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendoreventapplication.Models.RatingModel;
import com.example.vendoreventapplication.R;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RecyclerViewHolder> {
    Context context;
    private ArrayList<RatingModel> items;

    public RatingAdapter(Context context, ArrayList<RatingModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_item,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RatingModel currentItem = items.get(position);
        holder.rating_item_username.setText(currentItem.getUserName());
        holder.rating_item_mobile.setText(currentItem.getMobile());
        holder.rating_item_email.setText(currentItem.getEmail());
        holder.rating_item_rate.setRating(Float.parseFloat(currentItem.getRating()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public FeedbackAdapter.ClickListener clickListener;
        RelativeLayout relativeLayout;
        TextView rating_item_username,rating_item_mobile,rating_item_email;
        RatingBar rating_item_rate;
        Button view_rating_btn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            rating_item_username = itemView.findViewById(R.id.rating_item_username);
            rating_item_mobile = itemView.findViewById(R.id.rating_item_mobile);
            rating_item_email = itemView.findViewById(R.id.rating_item_email);
            rating_item_rate = itemView.findViewById(R.id.rating_item_rate);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

}
