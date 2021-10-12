package com.example.vendoreventapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vendoreventapplication.Activities.MainActivity;
import com.example.vendoreventapplication.Models.AddDecorationModel;
import com.example.vendoreventapplication.Models.EventModel;
import com.example.vendoreventapplication.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<AddDecorationModel> listdata;
    private final ClickListener mclickListener;
    public EventAdapter(ClickListener mclickListener, List<AddDecorationModel> listdata) {
        this.mclickListener= mclickListener;
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.event_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem,mclickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        final AddDecorationModel currentItem = listdata.get(position);

        holder.rv_eventName.setText(currentItem.getSelect_event());
        Log.e("event name :","onclcik :"+currentItem.getSelect_event());
        holder.rv_decorationeName.setText(currentItem.getDecoration_name());

        String mBase64string = currentItem.getDecoration_pic_image()    ;
        byte[] imageAsBytes = Base64.decode(mBase64string.getBytes(), Base64.DEFAULT);
        holder.decorationImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        String startPrice = String.valueOf(currentItem.getEvent_start_price());
        holder.money_start.setText(startPrice);
        String upToPrice = String.valueOf(currentItem.getEvent_upto_price());
        holder.money_end.setText(upToPrice);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public  ClickListener clickListener;
        TextView rv_eventName,rv_decorationeName,money_start,money_end;
        ImageView decorationImage;
        RelativeLayout relativeLayout;
        Button update_btn,delete_btn;

        public ViewHolder(@NonNull View itemView,ClickListener clickListener) {
            super(itemView);
            this.rv_eventName = (TextView)itemView.findViewById(R.id.rv_eventName);
            this.rv_decorationeName = (TextView)itemView.findViewById(R.id.rv_decorationeName);
            this.decorationImage = (ImageView)itemView.findViewById(R.id.decorationImage);
            this.money_start = (TextView)itemView.findViewById(R.id.money_start);
            this.money_end = (TextView)itemView.findViewById(R.id.money_end);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            this.update_btn = (Button)itemView.findViewById(R.id.update_btn);
            this.delete_btn = (Button)itemView.findViewById(R.id.delete_btn);
            this.clickListener=clickListener;
            update_btn.setOnClickListener(new View.OnClickListener() {
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
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onDeleteButtonClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onButtonClick(getAdapterPosition());
            clickListener.onDeleteButtonClick(getAdapterPosition());
        }

    }
    public interface ClickListener {
        void onButtonClick( int position);
        void onDeleteButtonClick( int position);
    }

}
