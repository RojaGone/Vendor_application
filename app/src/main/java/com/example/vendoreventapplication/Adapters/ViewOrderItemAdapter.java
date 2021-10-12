package com.example.vendoreventapplication.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendoreventapplication.Models.ViewOrderItemModel;
import com.example.vendoreventapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewOrderItemAdapter extends FirebaseRecyclerAdapter<ViewOrderItemModel,ViewOrderItemAdapter.ViewHolder> {

    private final ClickListener clickListener;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private final Context context;

    public ViewOrderItemAdapter(@NonNull FirebaseRecyclerOptions<ViewOrderItemModel> options, ClickListener clickListener, Context context) {
        super(options);
        this.clickListener = clickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewOrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.view_orders_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem,clickListener);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ViewOrderItemModel model) {



        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
        databaseReference1.child("UsersRegistration").orderByChild("customerID").equalTo(model.getCustomerID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.e("getIsOrderConfirm in adapter","onclick :"+model.getIsOrderConfirm());
                    if (model.getIsOrderConfirm().equals("2") || model.getIsOrderConfirm().equals("1")) {
                        String fname = ds.child("fname").getValue(String.class);
                        Log.e("fname in adapter ", "onclick :" + fname);
                        String lname = ds.child("lname").getValue(String.class);
                        String mobile = ds.child("mobile").getValue(String.class);
                        String email = ds.child("email").getValue(String.class);
                        String address = ds.child("address").getValue(String.class);

                        databaseReference1.child("AddDecoration").orderByChild("addDecorationID").equalTo(model.getAddDecorationID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String event_name = "";
                                String decoration_name = "";
                                String event_start_price = "";
                                String event_upto_price = "";
                                String decoration_pic_image = "";
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    event_name = ds.child("select_event").getValue(String.class);
                                    decoration_name = ds.child("decoration_name").getValue(String.class);
                                    event_start_price = ds.child("event_start_price").getValue(String.class);
                                    event_upto_price = ds.child("event_upto_price").getValue(String.class);
                                    decoration_pic_image = ds.child("decoration_pic_image").getValue(String.class);
                                    Log.e("event_name new1 1900", "onclick:" + event_name);

                                    sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("fname", fname);
                                    editor.commit();
                                    Log.e("get fname in adapter: ","onclick:"+fname);

                                    holder.voi_userName.setText(fname);
                                    holder.voi_eventName.setText(event_name);
                                    holder.voi_decorationeName.setText(decoration_name);

                                    String mBase64string = decoration_pic_image;
                                    byte[] imageAsBytes = Base64.decode(mBase64string.getBytes(), Base64.DEFAULT);
                                    holder.voi_decorationImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));


                                    holder.voi_money_end.setText(event_start_price);
                                    holder.voi_money_start.setText(event_upto_price);

                                    Log.e(" currentItem getIsOrderConfirm adapter", "onclick :" + model.getIsOrderConfirm());
                                    if (model.getIsOrderConfirm().equals("1")) {
                                        holder.accepted_btn.setVisibility(View.VISIBLE);
                                        holder.accept_btn.setVisibility(View.GONE);
                                        holder.relativeLayout.setBackgroundResource(R.drawable.color_bg);
                                        holder.reject_btn.setVisibility(View.GONE);
                                        holder.delete_btn.setVisibility(View.VISIBLE);
                                        holder.accept_btn.setEnabled(false);
                                    } else {

                                        holder.accept_btn.setVisibility(View.VISIBLE);
                                        holder.accepted_btn.setVisibility(View.GONE);
                                        holder.relativeLayout.setBackgroundResource(R.drawable.gray_stoke_bg);
                                        holder.reject_btn.setVisibility(View.VISIBLE);
                                        holder.delete_btn.setVisibility(View.GONE);
                                    }

                                    holder.reject_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String isOrderConfirm = "0";
                                            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                            DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
                                            databaseReference1.child("SetEventDetails").child(model.getEventDetailsID()).child("isOrderConfirm").setValue(isOrderConfirm);
                                            //ekkada user ki mail send cheyali your order rejected ani
                                        }
                                    });

                                    holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String isOrderConfirmAccept = "1";
                                            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                            DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
                                            databaseReference1.child("SetEventDetails").child(model.getEventDetailsID()).child("isOrderConfirm").setValue(isOrderConfirmAccept);
                                            Log.e("accepted", "onclick:");

                                        }
                                    });

                                    holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                            DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
                                            databaseReference1.child("SetEventDetails").child(model.getEventDetailsID()).removeValue();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{

                        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                        params.height = 0;
                        holder.itemView.setLayoutParams(params);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    @Override
//    public int getItemCount() {
//        return listdata.size();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView voi_userName,voi_eventName,voi_decorationeName,voi_money_start,voi_money_end,svo_firstName;
        ImageView voi_decorationImage;
        RelativeLayout relativeLayout;
        Button accept_btn,view_btn,delete_btn,reject_btn,accepted_btn;
        public ViewOrderItemAdapter.ClickListener clickListener;

        public ViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);

            this.voi_userName = (TextView) itemView.findViewById(R.id.voi_userName);
            this.voi_eventName = (TextView) itemView.findViewById(R.id.voi_eventName);
            this.voi_decorationeName = (TextView) itemView.findViewById(R.id.voi_decorationeName);
            this.voi_decorationImage= (ImageView) itemView.findViewById(R.id.voi_decorationImage);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.voi_money_start = (TextView) itemView.findViewById(R.id.voi_money_start);
            this.voi_money_end = (TextView) itemView.findViewById(R.id.voi_money_end);
            this.accept_btn = (Button) itemView.findViewById(R.id.accept_btn);
            this.view_btn = (Button) itemView.findViewById(R.id.view_btn);
            this.delete_btn = (Button) itemView.findViewById(R.id.delete_btn);
            this.reject_btn = (Button) itemView.findViewById(R.id.reject_btn);
            this.accepted_btn = (Button) itemView.findViewById(R.id.accepted_btn);


            view_btn.setOnClickListener(new View.OnClickListener() {
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
        void onButtonClick(int position);
    }
}
