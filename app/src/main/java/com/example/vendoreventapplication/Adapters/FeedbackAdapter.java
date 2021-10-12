package com.example.vendoreventapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendoreventapplication.Models.FeedbackModel;
import com.example.vendoreventapplication.R;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.RecyclerViewHolder> {
    Context context;
    private ArrayList<FeedbackModel> items;
    private final ClickListener listener;

    public FeedbackAdapter(Context context, ArrayList<FeedbackModel> items, ClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,listener);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        FeedbackModel currentItem = items.get(position);
        holder.fb_item_username.setText(currentItem.getUserName());
        holder.fb_item_mobile.setText(currentItem.getMobile());
        holder.fb_item_Email.setText(currentItem.getUserEmail());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ClickListener clickListener;
        TextView fb_item_username,fb_item_mobile,fb_item_Email;
        RelativeLayout relativeLayout;
        Button view_feedback_btn;

        public RecyclerViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            fb_item_username =itemView.findViewById(R.id.fb_item_username);
            fb_item_mobile = itemView.findViewById(R.id.fb_item_mobile);
            fb_item_Email = itemView.findViewById(R.id.fb_item_Email);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            view_feedback_btn = itemView.findViewById(R.id.view_feedback_btn);

            view_feedback_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onButtonClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            clickListener.onButtonClick(getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onButtonClick( int position);

    }
}
