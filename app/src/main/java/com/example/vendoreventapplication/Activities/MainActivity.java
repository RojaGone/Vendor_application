package com.example.vendoreventapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaos.view.PinView;
import com.example.vendoreventapplication.Adapters.EventAdapter;
import com.example.vendoreventapplication.Adapters.FeedbackAdapter;
import com.example.vendoreventapplication.Adapters.FeedbackAdapter.ClickListener;
import com.example.vendoreventapplication.Adapters.RatingAdapter;
import com.example.vendoreventapplication.Adapters.ViewOrderItemAdapter;
import com.example.vendoreventapplication.Models.AddDecorationModel;
import com.example.vendoreventapplication.Models.AddEvent;
import com.example.vendoreventapplication.Models.FeedbackModel;
import com.example.vendoreventapplication.Models.GoogleUserModel;
import com.example.vendoreventapplication.Models.RatingModel;
import com.example.vendoreventapplication.Models.User;
import com.example.vendoreventapplication.Models.ViewOrderItemModel;
import com.example.vendoreventapplication.Models.ViewOrder_view_model;
import com.example.vendoreventapplication.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roacult.backdrop.BackdropLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.vendoreventapplication.Activities.RegistrationActivity.PASSWORD_PATTERN;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private Toolbar toolbar;
    BackdropLayout backdropLayout;
    View back_layout,front_layout;
    Button dashboard,myProfile,changePass,addEvent,addDecoration,viewAllEvents,viewOrders,viewFeedback,viewRatings,logout;
    RelativeLayout rl_dashboard,rl_myProflie,rl_change_password,rl_add_decoration,rl_add_Event,rl_view_all_events,rl_view_orders,
            rl_view_feedback,rl_view_ratings,rl_single_view_order,rl_otp_code_verification,rl_new_password,
            rl_password_updated,rl_update_event,rl_feedback_view_details,rl_edit_myprofile;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private ViewOrderItemAdapter viewOrderItemAdapter;
    private Button change_pass_btn,decoration_pin_image_btn,chooseProfile_pic_btn;
    private TextInputEditText change_pass_phone,newPassword,newConfirmPassword,decoration_name,event_start_price,event_upto_price;
    private PinView pin_num_view;
    ArrayList<Integer> pageHistory = new ArrayList<Integer>();
    Integer currentPageId=1,dashboardPageId=2,myProfilePageId=3,changePassPageId=4,addDecorationPageId=5,addEventPageId=6,
            viewAllEventsPageId=7,viewOrdersPageId=8,viewFeedbackPageId=9,viewRatingsPageId=10,ViewPageId=11,ChangePasswordPageId=12,UpdateEventId=13,FeedbackViewDetailsId=14,RatingViewDetailsId=16,editProfilePageId=15;
    public static final Pattern MOBILE_NO=Pattern.compile(
            //   "(?=.*[0-9])"+
            //  ".{10,}"
            "(0/91)?[5-9][0-9]{9}"
    );
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser users;
    private String userID;
    private TextView myprofile_fname1,myprofile_lname1,myprofile_address1,myprofile_phone1,myprofile_email1,myprofile_gender1,
            myprofile_password1,svo_firstName,svo_lname,svo_eventName,svo_decorationName,svo_price,svo_orderDate,svo_orderTime,
            svo_mobileNo,svo_email,svo_address,svo_description,Today_orders,Total_orders,Accepted_orders,myprofile_address,
            myprofile_phone,myprofile_gender,myprofile_password;
    private CircleImageView my_profile_image;
    private TextInputEditText event_name,UE_decoration_name,UE_event_start_price,UE_event_upto_price;
    private Button addEvent_btn,otp_btn,newPassword_btn,add_decoration_btn,UE_pic_image_btn,updateEvent_btn,editProfile_btn,editProfile_btn1,editMyProfile_btn,editProfile_choosePic_btn;
    ProgressBar addEvent_progressbar;
    String mVerificationId;
    List<String> services;
    ImageView decoration_pic_image,UE_pic_image,svo_decorationImage,editprofile_image;
    RadioGroup station_service,designers,stay_travel,UE_station_service,UE_designers,UE_stay_travel,editProfile_RGroup;
    private String recentlySelectedImage ;
    private String picture_str="";
    private static final int  IMAGE_PIC_CODE=1000;
    private static final int PERMISSION_CODE=1000;
    private StorageReference storageReference;
    String txt_select_event="";
    AutoCompleteTextView select_event,UE_select_event;
    ArrayAdapter<String> adapters;
    ArrayList<AddDecorationModel> item;
    OutputStream outputStream;
    ProgressBar progressBar,UE_progressbar,AddDecor_progressbar,feedback_progressbar,progressbar_editprofile;
    RelativeLayout relativeLayout,UE_prog_relay,rating_prog_relay,feedback_prog_relay;
    String vendorEmail="";
    RadioButton UE_radioButton1,UE_radioButton2,UE_radioButton3,UE_radioButton4,UE_radioButton5,UE_radioButton6,editprofile_gender_male,editprofile_gender_female;
    String Update_select_event;
    String UE_selected_event;
    String UE_updatedPic;
    ArrayList<ViewOrder_view_model> viewOrder_view_models ;
    ArrayList<FeedbackModel> item_feedback;
    ArrayList<RatingModel> item_rating;
    FeedbackAdapter feedbackAdapter;
    RatingAdapter ratingAdapter;
    private TextView fb_view_feedback;
    private EditText editprofile_fname,editprofile_lname,editprofile_email,editprofile_address,editprofile_phone;
    private CheckBox fb_checkBox1,fb_checkBox2,fb_checkBox3,fb_checkBox4,fb_checkBox5,fb_checkBox6,fb_checkBox7,fb_checkBox8;
    private RatingBar rt_view_rating;
    ArrayList<String> isOrderConfirm=new ArrayList<String>();
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String MyProfile_Password;
    String MyProfile_currentDate;
    String MyProfile_isConfirm;
    String MyProfile_Picture;
    String Fname;
    String Lname;
    String Email;
    String Address;
    String Gender;
    String Mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        backdropLayout=findViewById(R.id.backdropLayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backdropLayout =  (BackdropLayout)findViewById(R.id.backdropLayout);
        back_layout = backdropLayout.getChildAt(0);
        front_layout = backdropLayout.getChildAt(1);

        dashboard = (Button)back_layout.findViewById(R.id.dashboard);
        myProfile = (Button)back_layout.findViewById(R.id.myProfile);
        changePass = (Button)back_layout.findViewById(R.id.changePass);
        addEvent = (Button)back_layout.findViewById(R.id.addEvent);
        addDecoration = (Button)back_layout.findViewById(R.id.addDecoration);
        viewAllEvents = (Button)back_layout.findViewById(R.id.viewAllEvents);
        viewOrders = (Button)back_layout.findViewById(R.id.viewOrders);
        viewFeedback = (Button)back_layout.findViewById(R.id.viewFeedback);
        viewRatings = (Button)back_layout.findViewById(R.id.viewRatings);
        logout = (Button)back_layout.findViewById(R.id.logout);
        change_pass_btn = (Button) findViewById(R.id.change_pass_btn);

        rl_dashboard = (RelativeLayout)findViewById(R.id.rl_dashboard);
        rl_myProflie = (RelativeLayout)findViewById(R.id.rl_myProflie);
        rl_change_password = (RelativeLayout)findViewById(R.id.rl_change_password);
        rl_add_decoration = (RelativeLayout)findViewById(R.id.rl_add_decoration);
        rl_add_Event = (RelativeLayout)findViewById(R.id.rl_add_Event);
        rl_view_all_events = (RelativeLayout)findViewById(R.id.rl_view_all_events);
        rl_view_orders = (RelativeLayout)findViewById(R.id.rl_view_orders);
        rl_view_feedback = (RelativeLayout)findViewById(R.id.rl_view_feedback);
        rl_view_ratings = (RelativeLayout)findViewById(R.id.rl_view_ratings);
        rl_single_view_order = (RelativeLayout)findViewById(R.id.rl_single_view_order);
        rl_otp_code_verification = (RelativeLayout)findViewById(R.id.rl_otp_code_verification);
        rl_new_password = (RelativeLayout)findViewById(R.id.rl_new_password);
        rl_password_updated = (RelativeLayout)findViewById(R.id.rl_password_updated);
        rl_update_event = (RelativeLayout)findViewById(R.id.rl_update_event);
        rl_feedback_view_details = (RelativeLayout)findViewById(R.id.rl_feedback_view_details);
        rl_edit_myprofile = (RelativeLayout)findViewById(R.id.rl_edit_myprofile);

        change_pass_phone =(TextInputEditText)findViewById(R.id.change_pass_phone);
        pin_num_view = (PinView)findViewById(R.id.pin_num_view);

        myprofile_fname1 = (TextView)findViewById(R.id.myprofile_fname1);
        myprofile_lname1 = (TextView)findViewById(R.id.myprofile_lname1);
        myprofile_address1 = (TextView)findViewById(R.id.myprofile_address1);
        myprofile_address = (TextView)findViewById(R.id.myprofile_address);
        myprofile_phone1 = (TextView)findViewById(R.id.myprofile_phone1);
        myprofile_phone = (TextView)findViewById(R.id.myprofile_phone);
        myprofile_email1 = (TextView)findViewById(R.id.myprofile_email1);
        myprofile_gender1 = (TextView)findViewById(R.id.myprofile_gender1);
        myprofile_gender = (TextView)findViewById(R.id.myprofile_gender);
        myprofile_password1 = (TextView)findViewById(R.id.myprofile_password1);
        myprofile_password = (TextView)findViewById(R.id.myprofile_password);
        my_profile_image = (CircleImageView)findViewById(R.id.my_profile_image);
        editProfile_btn = (Button)findViewById(R.id.editProfile_btn);
        editProfile_btn1 = (Button)findViewById(R.id.editProfile_btn1);
        progressbar_editprofile = (ProgressBar)findViewById(R.id.progressbar_editprofile);
        progressbar_editprofile.setVisibility(View.GONE);

        event_name = (TextInputEditText)findViewById(R.id.event_name);
        addEvent_btn = (Button)findViewById(R.id.addEvent_btn);
        addEvent_progressbar = (ProgressBar)findViewById(R.id.addEvent_progressbar);
        addEvent_progressbar.setVisibility(View.GONE);

        otp_btn = (Button)findViewById(R.id.otp_btn);
        pin_num_view =(PinView)findViewById(R.id.pin_num_view);

        newPassword = (TextInputEditText)findViewById(R.id.newPassword);
        newConfirmPassword = (TextInputEditText)findViewById(R.id.newConfirmPassword);
        newPassword_btn = (Button)findViewById(R.id.newPassword_btn);

        decoration_name = (TextInputEditText)findViewById(R.id.decoration_name);
        event_start_price = (TextInputEditText)findViewById(R.id.event_start_price);
        event_upto_price = (TextInputEditText)findViewById(R.id.event_upto_price);
        decoration_pin_image_btn = (Button)findViewById(R.id.decoration_pin_image_btn);
        decoration_pic_image = (ImageView)findViewById(R.id.decoration_pic_image);
        add_decoration_btn = (Button)findViewById(R.id.add_decoration_btn);
        station_service = (RadioGroup)findViewById(R.id.station_service);
        designers = (RadioGroup)findViewById(R.id.designers);
        stay_travel = (RadioGroup)findViewById(R.id.stay_travel);
        progressBar=findViewById(R.id.progressbar);
        relativeLayout=findViewById(R.id.prog_relay);
        AddDecor_progressbar = findViewById(R.id.AddDecor_progressbar);

        UE_select_event = (AutoCompleteTextView)findViewById(R.id.UE_select_event);
        UE_decoration_name = (TextInputEditText)findViewById(R.id.UE_decoration_name);
        UE_event_start_price = (TextInputEditText)findViewById(R.id.UE_event_start_price);
        UE_event_upto_price = (TextInputEditText)findViewById(R.id.UE_event_upto_price);
        UE_pic_image_btn = (Button)findViewById(R.id.UE_pic_image_btn);
        UE_station_service = (RadioGroup)findViewById(R.id.UE_station_service);
        UE_designers = (RadioGroup)findViewById(R.id.UE_designers);
        UE_stay_travel = (RadioGroup)findViewById(R.id.UE_stay_travel);
        updateEvent_btn = (Button)findViewById(R.id.updateEvent_btn);
        UE_prog_relay = (RelativeLayout)findViewById(R.id.UE_prog_relay);
        UE_progressbar = (ProgressBar)findViewById(R.id.UE_progressbar);
        UE_pic_image = (ImageView)findViewById(R.id.UE_pic_image);

        svo_firstName = (TextView)findViewById(R.id.svo_firstName);
        svo_lname = (TextView)findViewById(R.id.svo_lname);
        svo_eventName = (TextView)findViewById(R.id.svo_eventName);
        svo_decorationName = (TextView)findViewById(R.id.svo_decorationName);
        svo_price = (TextView)findViewById(R.id.svo_price);
        svo_orderDate = (TextView)findViewById(R.id.svo_orderDate);
        svo_orderTime = (TextView)findViewById(R.id.svo_orderTime);
        svo_mobileNo = (TextView)findViewById(R.id.svo_mobileNo);
        svo_email = (TextView)findViewById(R.id.svo_email);
        svo_address = (TextView)findViewById(R.id.svo_address);
        svo_description = (TextView)findViewById(R.id.svo_description);
        svo_decorationImage = (ImageView)findViewById(R.id.svo_decorationImage);

        feedback_prog_relay= (RelativeLayout)findViewById(R.id.feedback_prog_relay);
        feedback_progressbar = (ProgressBar)findViewById(R.id.feedback_progressbar);
        fb_view_feedback = (TextView)findViewById(R.id.fb_view_feedback);
        fb_checkBox1 = (CheckBox)findViewById(R.id.fb_checkBox1);
        fb_checkBox2 = (CheckBox)findViewById(R.id.fb_checkBox2);
        fb_checkBox3 = (CheckBox)findViewById(R.id.fb_checkBox3);
        fb_checkBox4 = (CheckBox)findViewById(R.id.fb_checkBox4);
        fb_checkBox5 = (CheckBox)findViewById(R.id.fb_checkBox5);
        fb_checkBox6 = (CheckBox)findViewById(R.id.fb_checkBox6);
        fb_checkBox7 = (CheckBox)findViewById(R.id.fb_checkBox7);
        fb_checkBox8 = (CheckBox)findViewById(R.id.fb_checkBox8);

        editprofile_fname = (EditText) findViewById(R.id.editprofile_fname);
        editprofile_lname = (EditText)findViewById(R.id.editprofile_lname);
        editprofile_email = (EditText)findViewById(R.id.editprofile_email);
        editprofile_address = (EditText)findViewById(R.id.editprofile_address);
        editprofile_phone = (EditText)findViewById(R.id.editprofile_phone);
        editprofile_gender_male = (RadioButton)findViewById(R.id.editprofile_gender_male);
        editprofile_gender_female = (RadioButton)findViewById(R.id.editprofile_gender_female);
        editprofile_image = (ImageView)findViewById(R.id.editprofile_image);
        editProfile_choosePic_btn = (Button)findViewById(R.id.editProfile_choosePic_btn);
        editMyProfile_btn = (Button)findViewById(R.id.editMyProfile_btn);
        editProfile_RGroup = (RadioGroup)findViewById(R.id.editProfile_RGroup);

        Today_orders = (TextView)findViewById(R.id.Today_orders);
        Total_orders = (TextView)findViewById(R.id.Total_orders);
        Accepted_orders = (TextView)findViewById(R.id.Accepted_orders);

        auth = FirebaseAuth.getInstance();
        users =  auth.getCurrentUser();
        userID =users.getUid();
        Log.e("user id in mainactivity 306 :","onclick :"+userID);

//        Intent intent = getIntent();
//        String uId = intent.getStringExtra("Uid");
//        Log.e("user1 id :","onclick :"+uId);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);



        pageHistory.add(rl_dashboard.getId());
        currentPageId = rl_dashboard.getId();
        setMainLayoutVisible(rl_dashboard);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Email = snapshot.child("email").getValue(String.class);
                vendorEmail = Email;
                Log.e("vendor Email1 :","onclick :"+vendorEmail);

                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
                databaseReference1.child("SetEventDetails").orderByChild("vendorEmail").equalTo(vendorEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        int count_todayOrders = 0;
                        int count_acceptedOrders = 0;
                        int i=0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String isOrderConfirmL = ds.child("isOrderConfirm").getValue(String.class);
                                isOrderConfirm.add(isOrderConfirmL);
                                Log.e(" isOrderConfirm1 1852", "onclick :" + isOrderConfirm);
                                i++;
                                String isOrderConfirm12 = ds.child("isOrderConfirm").getValue(String.class);
                                String currentDate = ds.child("currentDate").getValue(String.class);

                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                                Calendar c = Calendar.getInstance();
                                String date = sdf.format(c.getTime());

                                if (isOrderConfirm12.equals("1") || isOrderConfirm12.equals("2")) {
                                    count++;
                                }
                                if (currentDate.equals(date)) {
                                    count_todayOrders++;
                                }
                                if (isOrderConfirm12.equals("1")) {
                                    count_acceptedOrders++;
                                }
                            }
                            Today_orders.setText(String.valueOf(count_todayOrders));
                            Total_orders.setText(String.valueOf(count));
                            Accepted_orders.setText(String.valueOf(count_acceptedOrders));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        rl_dashboard.setVisibility(View.VISIBLE);
//        rl_myProflie.setVisibility(View.GONE);
//        rl_add_service.setVisibility(View.GONE);
//        rl_add_service.setVisibility(View.GONE);
//        rl_view_all_services.setVisibility(View.GONE);
//        rl_view_orders.setVisibility(View.GONE);
//        rl_view_feedback.setVisibility(View.GONE);
//        rl_view_ratings.setVisibility(View.GONE);
//        rl_view_customer_details.setVisibility(View.GONE);



        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardMethod();
                pageHistory.add(rl_dashboard.getId());
                dashboardPageId = rl_dashboard.getId();
                System.out.println("dash id :"+dashboardPageId);

            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProfileMethod();
                pageHistory.add(rl_myProflie.getId());
                myProfilePageId = rl_myProflie.getId();
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_pass_phone.setText("");
                changePassMethod();
                pageHistory.add(rl_change_password.getId());
                changePassPageId = rl_change_password.getId();

            }
        });
        addDecoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDecor_progressbar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                select_event= findViewById(R.id.select_event);
                select_event.setText("");
                decoration_name.setText("");
                event_start_price.setText("");
                event_upto_price.setText("");
                decoration_pic_image.setImageResource(R.drawable.ic_profile);
                RadioButton radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
                radioButton1.setChecked(false);
                RadioButton radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
                radioButton2.setChecked(false);
                RadioButton radioButton3 = (RadioButton)findViewById(R.id.radioButton3);
                radioButton3.setChecked(false);
                RadioButton radioButton4 = (RadioButton)findViewById(R.id.radioButton4);
                radioButton4.setChecked(false);
                RadioButton radioButton5 = (RadioButton)findViewById(R.id.radioButton5);
                radioButton5.setChecked(false);
                RadioButton radioButton6 = (RadioButton)findViewById(R.id.radioButton6);
                radioButton6.setChecked(false);
                addDecorationMethod();
                pageHistory.add(rl_add_decoration.getId());
                addDecorationPageId = rl_add_decoration.getId();

            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventMethod();
                pageHistory.add(rl_add_Event.getId());
                addEventPageId = rl_add_Event.getId();
            }
        });
        viewAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAllEventsMethod();
                pageHistory.add(rl_view_all_events.getId());
                viewAllEventsPageId = rl_view_all_events.getId();

            }
        });
        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageHistory.add(rl_view_orders.getId());
                viewOrdersPageId = rl_view_orders.getId();
                ViewOrdersMethod();
            }
        });
        viewFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFeedbackMethod();
                pageHistory.add(rl_view_feedback.getId());
                viewFeedbackPageId = rl_view_feedback.getId();

            }
        });
        viewRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRatingsMethod();
                pageHistory.add(rl_view_ratings.getId());
                viewRatingsPageId = rl_view_ratings.getId();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Google_signOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });


    }

    private void editProfile_method() {

        backdropLayout.close();
        pageHistory.add(rl_edit_myprofile.getId());
        editProfilePageId = rl_edit_myprofile.getId();
        setMainLayoutVisible(rl_edit_myprofile);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_dashboard);

        editProfile_choosePic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        //permission not granted
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show pop up
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallary();
                    }
                }
                else{
                    //system os is less then
                    pickImageFromGallary();
                }
            }
        });

        editMyProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar_editprofile.setVisibility(View.VISIBLE);
                String editprofile_fname_text = editprofile_fname.getText().toString();
                String editprofile_lname_text = editprofile_lname.getText().toString();
                String editprofile_email_text = editprofile_email.getText().toString();
                String editprofile_address_text = editprofile_address.getText().toString();
                String editprofile_phone_text = editprofile_phone.getText().toString();

                RadioButton checkedBtn2 = findViewById(editProfile_RGroup.getCheckedRadioButtonId());

                try{
                    String editProfile_gender_get = checkedBtn2.getText().toString();
                    if (validFirstname() == false || validLastname() == false || validateEmail() == false || validAddress() == false || validMobile_no() == false) {

                    } else {
                        String choose_Picture;
                        if (recentlySelectedImage == null){
                            choose_Picture = MyProfile_Picture;
                        }else {
                            choose_Picture = recentlySelectedImage;
                        }

                        String oldEmail = Email;
                        String oldPassword = MyProfile_Password;

                        auth.signInWithEmailAndPassword(oldEmail,oldPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseUser user =auth.getCurrentUser();
                                AuthCredential authCredential = EmailAuthProvider.getCredential(oldEmail,oldPassword);
                                user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        user.updateEmail(editprofile_email_text).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                                firebaseDatabase.getReference().child("VendorRegistration").child(user.getUid()).child("email").setValue(editprofile_email_text);
//                                                        Toast.makeText(getContext(), "email updated successfully.....", Toast.LENGTH_SHORT).show();

                                                String user_id = auth.getCurrentUser().getUid();
                                                Log.e("user id","onclick :"+user_id);
                                                User user =new User(userID,editprofile_fname_text ,editprofile_lname_text ,editprofile_email_text ,editprofile_address_text ,editprofile_phone_text ,MyProfile_Password,editProfile_gender_get ,choose_Picture ,MyProfile_isConfirm ,MyProfile_currentDate  );
                                                firebaseDatabase.getReference().child("UsersRegistration").child(user_id).setValue(user);
//                                                      Toast.makeText(RegistrationActivity.this, "image added succesfully in the storage", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(getApplicationContext(), "update succesfully", Toast.LENGTH_SHORT).show();
                                                progressbar_editprofile.setVisibility(View.GONE);
                                                myProfileMethod();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "1"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.e("1","onclick :"+e);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "2"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("2","onclick :"+e);
                                    }
                                });
                            }
                        });
                    }
                }catch (Exception e){
                    Log.e("User Exception reg5","Onclick"+e.getMessage());
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean validMobile_no(){

        String mobile_no = myprofile_phone1.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(mobile_no.isEmpty()){
            myprofile_phone1.setError("Field can't be empty");
            return false;
        }else if(!MOBILE_NO.matcher(mobile_no).matches()){
            myprofile_phone1.setError("please check you are number.");
            return false;
        }else{
            myprofile_phone1.setError(null);
            return true;
        }
    }

    private boolean validAddress(){

        String address= myprofile_address1.getText().toString().trim();
        if(address.isEmpty()){
            myprofile_address1.setError("Field can't be empty");
            return false;
        }
        return true;
    }

    private boolean validateEmail(){
        String email = myprofile_email1.getText().toString().trim();
        if(email.isEmpty()){
            myprofile_email1.setError("Field can't be empty");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            myprofile_email1.setError("please enter a valid email address");
            return false;
        }
        else{
            myprofile_email1.setError(null);
            return true;
        }
    }
    private boolean validLastname(){

        String lname = myprofile_lname1.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(lname.isEmpty()){
            myprofile_lname1.setError("Field can't be empty");
            return false;
        }else if(lname.length()>=15){
            myprofile_lname1.setError("name is too long");
            return false;
        }else if(lname.matches(noWhiteSpace)){
            myprofile_lname1.setError("White space are not allowed.");
            return false;
        }else{
            myprofile_lname1.setError(null);
            return true;
        }
    }

    private boolean validFirstname(){

        String fname = myprofile_fname1.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(fname.isEmpty()){
            myprofile_fname1.setError("Field can't be empty");
            return false;
        }else if(fname.length()>=15){
            myprofile_fname1.setError("name is too long");
            return false;
        }else if(fname.matches(noWhiteSpace)){
            myprofile_fname1.setError("White space are not allowed.");
            return false;
        }else{
            myprofile_fname1.setError(null);
            return true;
        }
    }

    private void dashboardMethod() {
        backdropLayout.close();
        setLayoutVisible(rl_dashboard);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Email = snapshot.child("email").getValue(String.class);
                vendorEmail = Email;
                Log.e("vendor Email1 :","onclick :"+vendorEmail);

                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
                databaseReference1.child("SetEventDetails").orderByChild("vendorEmail").equalTo(vendorEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        int count_todayOrders = 0;
                        int count_acceptedOrders = 0;
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            String isOrderConfirm = ds.child("isOrderConfirm").getValue(String.class);
                            String currentDate = ds.child("currentDate").getValue(String.class);
                            Log.e(" isOrderConfirm 608", "onclick :" + isOrderConfirm);
                            Log.e(" currentDate 609", "onclick :" + currentDate);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                            Calendar c = Calendar.getInstance();
                            String date = sdf.format(c.getTime());
                            Log.e("date", "onClick: " + date );

                            if(isOrderConfirm.equals("1") || isOrderConfirm.equals("2")){
                                count++;
                            }
                            if (currentDate.equals(date)){
                                count_todayOrders++;
                            }
                            if (isOrderConfirm.equals("1")){
                                count_acceptedOrders++;
                            }
                        }
                        Today_orders.setText(String.valueOf(count_todayOrders));
                        Total_orders.setText(String.valueOf(count));
                        Accepted_orders.setText(String.valueOf(count_acceptedOrders));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myProfileMethod() {
        backdropLayout.close();
        setLayoutVisible(rl_myProflie);
        setLayoutInvisible(rl_dashboard);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Fname = snapshot.child("fname").getValue(String.class);
                Lname = snapshot.child("lname").getValue(String.class);
                Email = snapshot.child("email").getValue(String.class);
                Address = snapshot.child("address").getValue(String.class);
                Gender = snapshot.child("gender").getValue(String.class);
                Mobile = snapshot.child("mobile").getValue(String.class);
                MyProfile_Password = snapshot.child("password").getValue(String.class);
                MyProfile_currentDate = snapshot.child("currentDate").getValue(String.class);
                MyProfile_isConfirm = snapshot.child("isConfirm").getValue(String.class);
                MyProfile_Picture = snapshot.child("picture").getValue(String.class);
//                        System.out.println("spinnerName3 is new : " + MyProfile_Picture);

                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if(signInAccount != null){
                    myprofile_fname1.setText(signInAccount.getGivenName());
                    myprofile_lname1.setText(signInAccount.getFamilyName());
                    myprofile_email1.setText(signInAccount.getEmail());
                    myprofile_address1.setVisibility(View.GONE);
                    myprofile_gender1.setVisibility(View.GONE);
                    myprofile_phone1.setVisibility(View.GONE);
                    myprofile_password1.setVisibility(View.GONE);
                    myprofile_address.setVisibility(View.GONE);
                    myprofile_gender.setVisibility(View.GONE);
                    myprofile_phone.setVisibility(View.GONE);
                    myprofile_password.setVisibility(View.GONE);
                    editProfile_btn.setVisibility(View.GONE);
                    editProfile_btn1.setVisibility(View.VISIBLE);

                    Uri image = signInAccount.getPhotoUrl();

                    Glide.with(getApplicationContext()).load(image).into(my_profile_image);

                    editProfile_btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }else {

                    myprofile_fname1.setText(Fname);
                    myprofile_lname1.setText(Lname);
                    myprofile_email1.setText(Email);
                    myprofile_address1.setText(Address);
                    myprofile_gender1.setText(Gender);
                    myprofile_phone1.setText(Mobile);
                    myprofile_password1.setText(MyProfile_Password);

                    String mBase64string = MyProfile_Picture;
                    byte[] imageAsBytes = Base64.decode(mBase64string.getBytes(), Base64.DEFAULT);
                    my_profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                    editProfile_btn.setVisibility(View.VISIBLE);
                    editProfile_btn1.setVisibility(View.GONE);

                    editProfile_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            editProfile_method();
                            editprofile_fname.setText(Fname);
                            editprofile_lname.setText(Lname);
                            editprofile_email.setText(Email);
                            editprofile_address.setText(Address);
                            editprofile_phone.setText(Mobile);
                            String gender = Gender;
                            if(gender.equals("Female")){
                                editprofile_gender_female.setChecked(true);
                                editprofile_gender_male.setChecked(false);
                            }else {
                                editprofile_gender_female.setChecked(false);
                                editprofile_gender_male.setChecked(true);
                            }

                            editprofile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("User Exception userpage0","Onclick"+error.getMessage());
            }
        });

    }


    private void changePassMethod() {
        backdropLayout.close();
        setLayoutVisible(rl_change_password);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_dashboard);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validPhone_no() == true){
                    pin_num_view.setText("");
                    pageHistory.add(rl_otp_code_verification.getId());
                    ChangePasswordPageId = rl_otp_code_verification.getId();
                    ChangePasswordMethod();

                }
            }
        });


    }

    private void ChangePasswordMethod() {
        int code = 91;
        String mobile = change_pass_phone.getEditableText().toString();
        String _phoneNo = "+"+code+change_pass_phone.getEditableText().toString();
        Toast.makeText(MainActivity.this, "number :"+_phoneNo, Toast.LENGTH_SHORT).show();
        Log.e("mobile is :","onclick :"+mobile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Mobile = snapshot.child("mobile").getValue(String.class);
                if(mobile.equals(Mobile)){
                    backdropLayout.close();
                    setLayoutVisible(rl_otp_code_verification);
                    setLayoutInvisible(rl_myProflie);
                    setLayoutInvisible(rl_dashboard);
                    setLayoutInvisible(rl_add_decoration);
                    setLayoutInvisible(rl_view_all_events);
                    setLayoutInvisible(rl_view_orders);
                    setLayoutInvisible(rl_view_feedback);
                    setLayoutInvisible(rl_view_ratings);
                    setLayoutInvisible(rl_add_Event);
                    setLayoutInvisible(rl_single_view_order);
                    setLayoutInvisible(rl_change_password);
                    setLayoutInvisible(rl_new_password);
                    setLayoutInvisible(rl_password_updated);
                    setLayoutInvisible(rl_update_event);
                    setLayoutInvisible(rl_feedback_view_details);
                    setLayoutInvisible(rl_edit_myprofile);

                    sendVerificationCodeToUser(_phoneNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                change_pass_phone.setError("no such user exit");
            }
        });

//                Query checkUser = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference("VendorRegistration").orderByChild("mobile").equalTo(mobile);
//        Log.e("check user is :","onclick :"+checkUser);
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//
//
//
//                }else {
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pin_num_view.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Blank field can not be processed", Toast.LENGTH_SHORT).show();
                }else if(pin_num_view.getText().toString().length() != 6){
                    Toast.makeText(MainActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }else {
//                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId,pin_num_view.getText().toString());
//                    signInWithPhoneAuthCredential(credential);

                    newPassword.setText("");
                    newConfirmPassword.setText("");
                    backdropLayout.close();
                    setLayoutVisible(rl_new_password);
                    setLayoutInvisible(rl_myProflie);
                    setLayoutInvisible(rl_otp_code_verification);
                    setLayoutInvisible(rl_add_decoration);
                    setLayoutInvisible(rl_view_all_events);
                    setLayoutInvisible(rl_view_orders);
                    setLayoutInvisible(rl_view_feedback);
                    setLayoutInvisible(rl_view_ratings);
                    setLayoutInvisible(rl_add_Event);
                    setLayoutInvisible(rl_single_view_order);
                    setLayoutInvisible(rl_change_password);
                    setLayoutInvisible(rl_dashboard);
                    setLayoutInvisible(rl_password_updated);
                    setLayoutInvisible(rl_update_event);
                    setLayoutInvisible(rl_feedback_view_details);
                    setLayoutInvisible(rl_edit_myprofile);

                }
            }
        });

        newPassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validNewPassword() == true) {

                    if (validNewConfirmPassword() == true) {

                        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String oldPassword = snapshot.child("password").getValue(String.class);
                                String oldEmail = snapshot.child("email").getValue(String.class);
                                String new_Password=newPassword.getText().toString();
                                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                System.out.println("the old password is : "+oldPassword);
                                System.out.println("the old email is : "+oldEmail);
                                AuthCredential authCredential = EmailAuthProvider.getCredential(oldEmail,oldPassword);
                                currentUser.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        currentUser.updatePassword(new_Password).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                User user =new User(new_Password);
                                                System.out.println("the new pass is : "+new_Password);
                                                mDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("VendorRegistration");
                                                mDatabase.child(auth.getUid()).child("password").setValue(new_Password);
                                                Toast.makeText(MainActivity.this, "password updated successfully.....", Toast.LENGTH_SHORT).show();
                                                backdropLayout.close();
                                                setLayoutVisible(rl_password_updated);
                                                setLayoutInvisible(rl_myProflie);
                                                setLayoutInvisible(rl_change_password);
                                                setLayoutInvisible(rl_add_decoration);
                                                setLayoutInvisible(rl_dashboard);
                                                setLayoutInvisible(rl_view_orders);
                                                setLayoutInvisible(rl_view_feedback);
                                                setLayoutInvisible(rl_view_ratings);
                                                setLayoutInvisible(rl_add_Event);
                                                setLayoutInvisible(rl_single_view_order);
                                                setLayoutInvisible(rl_otp_code_verification);
                                                setLayoutInvisible(rl_new_password);
                                                setLayoutInvisible(rl_view_all_events);
                                                setLayoutInvisible(rl_update_event);
                                                setLayoutInvisible(rl_feedback_view_details);
                                                setLayoutInvisible(rl_edit_myprofile);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "1"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.e("1","onclick :"+e);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "2"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("2","onclick :"+e);
                                    }
                                });

//                                            FirebaseAuth auth = FirebaseAuth.getInstance();
//                                            FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
//                                            users.updateEmail(oldEmail);
//                                            users.updatePassword(new_Password).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful()){
//                                                        User user =new User(new_Password);
//                                                        Log.e("new password is:","onclick :"+new_Password);
//                                                        mDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("VendorRegistration");
//                                                        mDatabase.child(userID).child("password").setValue(new_Password);
//                                                        Toast.makeText(MainActivity.this, "password updated successfully", Toast.LENGTH_SHORT).show();
//
//                                                        backdropLayout.close();
//                                                        setLayoutVisible(rl_password_updated);
//                                                        setLayoutInvisible(rl_myProflie);
//                                                        setLayoutInvisible(rl_change_password);
//                                                        setLayoutInvisible(rl_add_decoration);
//                                                        setLayoutInvisible(rl_dashboard);
//                                                        setLayoutInvisible(rl_view_orders);
//                                                        setLayoutInvisible(rl_view_feedback);
//                                                        setLayoutInvisible(rl_view_ratings);
//                                                        setLayoutInvisible(rl_add_Event);
//                                                        setLayoutInvisible(rl_single_view_order);
//                                                        setLayoutInvisible(rl_otp_code_verification);
//                                                        setLayoutInvisible(rl_new_password);
//                                                        setLayoutInvisible(rl_view_all_events);
//                                                        setLayoutInvisible(rl_update_event);
//                                                    }else {
//                                                        Toast.makeText(MainActivity.this, "password not updated", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }else {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(MainActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
            Log.e("TAG01", "onCodeSent:" + s);

            // Save verification ID and resending token so we can use them later
            mVerificationId = s;
            PhoneAuthProvider.ForceResendingToken mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.e("TAG", "onVerificationCompleted:" + phoneAuthCredential);
            String code = phoneAuthCredential.getSmsCode();
            Log.e("code is :","onclick :"+code);
            if(code!=null) {
                pin_num_view.setText(code);
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e("TAG0", "onVerificationFailed"+ e);
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }
        }
    };
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("TAG1", "signInWithCredential:success"+credential);


//                            FirebaseUser user = task.getResult().getUser();
//                            Toast.makeText(MainActivity.this, "signInWithCredential:success"+user, Toast.LENGTH_SHORT).show();


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.e("TAG2", "signInWithCredential:failure"+ task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
        });
    }

    private boolean validNewConfirmPassword() {
        String password = newPassword.getText().toString().trim();
        String confirm_pass = newConfirmPassword.getText().toString().trim();

        if(confirm_pass.isEmpty()){
            newConfirmPassword.setError("Field can't be empty");
            return false;
        }else if(!password.equals(confirm_pass)){
            newConfirmPassword.setError("Password Not matching");
            return false;
        }else {
            newConfirmPassword.setError(null);
            return true;
        }
    }

    private boolean validNewPassword() {
        String password=newPassword.getText().toString().trim();

        if(password.isEmpty()){
            newPassword.setError("Field can't be empty");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(password).matches()){
            newPassword.setError("please use special charecters and Do not use white space and it shold be 6 charecters. Ex:abc@123");
            return false;
        }
        else{
            newPassword.setError(null);
            return true;
        }
    }


    private void addDecorationMethod() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("AddEvent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                services = new ArrayList<String>();
//                services.add("Select Event");
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String spinnerName = ds.child("event_name").getValue(String.class);
                    System.out.println("spinnerName is new : " + spinnerName);
                    services.add(spinnerName);
                }

                select_event= findViewById(R.id.select_event);
                adapters= new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, services);
                adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_event.setAdapter(adapters);
                select_event.getText().toString();
                select_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), "OnItemSelectedListener : " +  adapters.getItem(i).toString(), Toast.LENGTH_SHORT).show();
                        txt_select_event =  select_event.getText().toString();
                        Log.e("select event is :","onclick :"+select_event.getText().toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        backdropLayout.close();
        setLayoutVisible(rl_add_decoration);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_dashboard);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        decoration_pin_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        //permission not granted
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show pop up
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallary();
                    }
                }
                else{
                    //system os is less then
                    pickImageFromGallary();
                }
            }
        });

        add_decoration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    add_decoration_btn.setEnabled(false);
                    AddDecor_progressbar.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    String txt_decoration_name = decoration_name.getText().toString();
                    String txt_event_start_price = event_start_price.getText().toString();
                    String txt_event_upto_price = event_upto_price.getText().toString();
                    RadioButton checkedBtn1 = findViewById(station_service.getCheckedRadioButtonId());
                    String txt_station_service = checkedBtn1.getText().toString();
                    RadioButton checkedBtn2 = findViewById(designers.getCheckedRadioButtonId());
                    String txt_designers = checkedBtn2.getText().toString();
                    RadioButton checkedBtn3 = findViewById(stay_travel.getCheckedRadioButtonId());
                    String txt_stay_travel = checkedBtn3.getText().toString();
                    String txt_picture = recentlySelectedImage;
                    firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String Email = snapshot.child("email").getValue(String.class);
                            vendorEmail = Email;
                            if (TextUtils.isEmpty(txt_station_service) || TextUtils.isEmpty(txt_designers) || TextUtils.isEmpty(txt_stay_travel) || TextUtils.isEmpty(txt_picture)) {
                                Toast.makeText(MainActivity.this, "Field can not be empty", Toast.LENGTH_SHORT).show();
                            }else if (validDecorationName() == true || validEventStartPrice() == true || validEventUptoPrice() == true) {

                                        addDecorationMethod(vendorEmail,txt_select_event, txt_decoration_name, txt_event_start_price, txt_event_upto_price, txt_station_service, txt_designers, txt_stay_travel, txt_picture);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }catch (NullPointerException e){
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                    add_decoration_btn.setEnabled(true);
                }
            }
        });
    }

    private void addDecorationMethod(String txt_email,String txt_select_event,String txt_decoration_name, String txt_event_start_price, String txt_event_upto_price, String txt_station_service, String txt_designers, String txt_stay_travel, String txt_picture) {
        try{
            Uri myUri = Uri.parse(picture_str);
            storageReference = FirebaseStorage.getInstance().getReference("decorationImages");
            StorageReference filereference = storageReference.child(txt_decoration_name+"."+getFileExtension(myUri));
            filereference.putFile(myUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("AddDecoration");
                    String id = mDatabase.push().getKey();
                    AddDecorationModel addDecorationModel = new AddDecorationModel(id,txt_email,txt_select_event,txt_decoration_name,txt_picture,txt_station_service,txt_designers,txt_stay_travel,txt_event_start_price,txt_event_upto_price);

                    mDatabase.child(id).setValue(addDecorationModel);
                    Toast.makeText(MainActivity.this, "Add decoration successful", Toast.LENGTH_SHORT).show();
                    add_decoration_btn.setEnabled(true);
                    select_event= findViewById(R.id.select_event);
                    select_event.setText("");
                    decoration_name.setText("");
                    event_start_price.setText("");
                    event_upto_price.setText("");
                    decoration_pic_image.setImageResource(R.drawable.ic_profile);
                    RadioButton radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
                    radioButton1.setChecked(false);
                    RadioButton radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
                    radioButton2.setChecked(false);
                    RadioButton radioButton3 = (RadioButton)findViewById(R.id.radioButton3);
                    radioButton3.setChecked(false);
                    RadioButton radioButton4 = (RadioButton)findViewById(R.id.radioButton4);
                    radioButton4.setChecked(false);
                    RadioButton radioButton5 = (RadioButton)findViewById(R.id.radioButton5);
                    radioButton5.setChecked(false);
                    RadioButton radioButton6 = (RadioButton)findViewById(R.id.radioButton6);
                    radioButton6.setChecked(false);
                    AddDecor_progressbar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                }
            });
        }catch (IllegalArgumentException e){
            Log.e("User Exception re3","Onclick"+e.getMessage());
        }

    }

    private boolean validEventUptoPrice() {
        progressBar.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
        String a_event_upto_price = event_upto_price.getText().toString().trim();

        if(a_event_upto_price.isEmpty()){
            event_upto_price.setError("Field can't be empty");
            return false;
        }else if(a_event_upto_price.length()>=15){
            event_upto_price.setError("Decoration name is too long");
            return false;
        }else{
            event_upto_price.setError(null);
            return true;
        }
    }

    private boolean validEventStartPrice() {
        progressBar.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
        String a_event_start_price = event_start_price.getText().toString().trim();

        if(a_event_start_price.isEmpty()){
            event_start_price.setError("Field can't be empty");
            return false;
        }else if(a_event_start_price.length()>=15){
            event_start_price.setError("Decoration name is too long");
            return false;
        }else{
            event_start_price.setError(null);
            return true;
        }
    }

    private boolean validDecorationName() {

        String a_decoration_name = decoration_name.getText().toString().trim();

        if(a_decoration_name.isEmpty()){
            decoration_name.setError("Field can't be empty");
            return false;
        }else if(a_decoration_name.length()>=35){
            decoration_name.setError("Decoration name is too long");
            return false;
        }else{
            decoration_name.setError(null);
            return true;
        }
    }

    private void pickImageFromGallary() {
        Intent intent  =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PIC_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    pickImageFromGallary();
                }
                else{
                    Toast.makeText(MainActivity.this,"permission denied.....!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //method to convert the selected image to base64 encoded string
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PIC_CODE) {
            decoration_pic_image.setImageURI(data.getData());
            UE_pic_image.setImageURI(data.getData());
            editprofile_image.setImageURI(data.getData());
            picture_str = data.getData().toString();
            Uri targetUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = this.getContentResolver().openInputStream(targetUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            recentlySelectedImage = encodeTobase64(yourSelectedImage);
            Log.e("recentlySelectedImage","onclcik:"+recentlySelectedImage);
            // kindha code store image in phoone storage file lo image store chesthadi
            Bitmap immagex=yourSelectedImage;
            File file= Environment.getExternalStorageDirectory();
            File dir = new File(file.getAbsolutePath()+"/DecorationImages/");
            dir.mkdir();
            File file1= new File(dir,System.currentTimeMillis()+".jpeg");
            try{
                outputStream= new FileOutputStream(file1);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            try {
                immagex.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }catch (NullPointerException e){
                Log.e("1426","onclick :"+e);
            }

        }
    }


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void addEventMethod() {
        backdropLayout.close();
        setLayoutVisible(rl_add_Event);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_dashboard);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
//      setLayoutInvisible(rl_view_customer_details);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        addEvent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent_progressbar.setVisibility(view.VISIBLE);
                String event_name1 =event_name.getText().toString();
                mDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("AddEvent");
                AddEvent addEvent = new AddEvent(event_name1);
                String id = mDatabase.push().getKey();
                mDatabase.child(id).setValue(addEvent);
                Toast.makeText(MainActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                addEvent_progressbar.setVisibility(view.GONE);
                event_name.setText("");
            }
        });
    }

    private void viewAllEventsMethod() {
        backdropLayout.close();
        setLayoutVisible(rl_view_all_events);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_dashboard);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
//      setLayoutInvisible(rl_view_customer_details);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String CurrentEmail = snapshot.child("email").getValue(String.class);
                Log.e("vendor Email :","onclick :"+CurrentEmail);

                firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");

                DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("AddDecoration");

                databaseReference1.orderByChild("vendorEmail").equalTo(CurrentEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.e("userlist.3211.0........","onclick...:"+snapshot.getValue());
                        item =new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            String cat=ds.child("select_event").getValue(String.class);
                            String subcat = ds.child("decoration_name").getValue(String.class);
                            String start_m= ds.child("event_start_price").getValue(String.class);
                            String end_m= ds.child("event_upto_price").getValue(String.class);
                            String pic=ds.child("decoration_pic_image").getValue(String.class);

                            AddDecorationModel addDecorationModel = ds.getValue(AddDecorationModel.class);
                            Log.e("cat :","onclick :"+cat);
                            Log.e("cat :","onclick :"+subcat);
                            Log.e("cat :","onclick :"+start_m);
                            Log.e("cat :","onclick :"+end_m);
                            Log.e("cat :","onclick :"+pic);
                            item.add(addDecorationModel);
                            Log.e("userlist.3211.........","onclick...:"+item);
                            recyclerView= findViewById(R.id.my_event_recycler);
//                    eventAdapter = new EventAdapter(getApplicationContext(),item);
                            eventAdapter = new EventAdapter( new EventAdapter.ClickListener() {
                                @Override
                                public void onButtonClick(int position) {
                                    Log.e("position is : ","onclick :"+position);
                                    UE_progressbar.setVisibility(View.GONE);
                                    UE_prog_relay.setVisibility(View.GONE);
                                    backdropLayout.close();
                                    pageHistory.add(rl_update_event.getId());
                                    UpdateEventId = rl_update_event.getId();
                                    setLayoutVisible(rl_update_event);
                                    setLayoutInvisible(rl_myProflie);
                                    setLayoutInvisible(rl_change_password);
                                    setLayoutInvisible(rl_add_decoration);
                                    setLayoutInvisible(rl_view_all_events);
                                    setLayoutInvisible(rl_view_orders);
                                    setLayoutInvisible(rl_dashboard);
                                    setLayoutInvisible(rl_view_ratings);
                                    setLayoutInvisible(rl_add_Event);
                                    setLayoutInvisible(rl_view_feedback);
                                    setLayoutInvisible(rl_otp_code_verification);
                                    setLayoutInvisible(rl_new_password);
                                    setLayoutInvisible(rl_password_updated);
                                    setLayoutInvisible(rl_single_view_order);
                                    setLayoutInvisible(rl_feedback_view_details);
                                    setLayoutInvisible(rl_edit_myprofile);

                                    UE_radioButton1 = (RadioButton)findViewById(R.id.UE_radioButton1);
                                    UE_radioButton2 = (RadioButton)findViewById(R.id.UE_radioButton2);
                                    UE_radioButton3 = (RadioButton)findViewById(R.id.UE_radioButton3);
                                    UE_radioButton4 = (RadioButton)findViewById(R.id.UE_radioButton4);
                                    UE_radioButton5 = (RadioButton)findViewById(R.id.UE_radioButton5);
                                    UE_radioButton6 = (RadioButton)findViewById(R.id.UE_radioButton6);

                                    AddDecorationModel addDecorationModel = item.get(position);
                                    UE_select_event.setText(addDecorationModel.getSelect_event());
                                    UE_decoration_name.setText(addDecorationModel.getDecoration_name());
                                    UE_event_start_price.setText(addDecorationModel.getEvent_start_price());
                                    UE_event_upto_price.setText(addDecorationModel.getEvent_upto_price());

                                    String mBase64string = addDecorationModel.getDecoration_pic_image();
                                    byte[] imageAsBytes = Base64.decode(mBase64string.getBytes(), Base64.DEFAULT);
                                    UE_pic_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                    if(addDecorationModel.getStation_service().equals("Yes")){
                                        UE_radioButton1.setChecked(true);
                                    }else {
                                        UE_radioButton2.setChecked(true);
                                    }
                                    if(addDecorationModel.getDesigners().equals("Yes")){
                                        UE_radioButton3.setChecked(true);
                                    }else {
                                        UE_radioButton4.setChecked(true);
                                    }
                                    if(addDecorationModel.getStay_travel().equals("Yes")){
                                        UE_radioButton5.setChecked(true);
                                    }else {
                                        UE_radioButton6.setChecked(true);
                                    }

                                    updateEvent_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try{
                                                UE_prog_relay.setVisibility(View.VISIBLE);
                                                UE_progressbar.setVisibility(View.VISIBLE);

                                                String UE_Decoration_name = UE_decoration_name.getText().toString();
                                                String UE_Event_start_price = UE_event_start_price.getText().toString();
                                                String UE_Event_upto_price = UE_event_upto_price.getText().toString();
                                                RadioButton checkedBtn1 = findViewById(UE_station_service.getCheckedRadioButtonId());
                                                String UE_Station_service = checkedBtn1.getText().toString();
                                                RadioButton checkedBtn2 = findViewById(UE_designers.getCheckedRadioButtonId());
                                                String UE_Designers = checkedBtn2.getText().toString();
                                                RadioButton checkedBtn3 = findViewById(UE_stay_travel.getCheckedRadioButtonId());
                                                String UE_Stay_travel = checkedBtn3.getText().toString();
                                                String UE_Picture = recentlySelectedImage;
                                                Log.e("recently selected image for update :","onclcick :"+recentlySelectedImage);

                                                String vendorEmailAddress=addDecorationModel.getVendorEmail();
                                                String addDecID = addDecorationModel.getAddDecorationID();

                                                if(Update_select_event == null ){
                                                    UE_selected_event =addDecorationModel.getSelect_event();
                                                    Log.e("selected_event name :","onclick  :"+UE_selected_event);
                                                } else{
                                                    UE_selected_event = Update_select_event;
                                                    Log.e("Update_select_event name :","onclick  :"+Update_select_event);
                                                }

                                                if (UE_Picture == null){
                                                    UE_updatedPic = mBase64string;
                                                    Log.e("UE_Picture1 name :","onclick  :"+mBase64string);

                                                }else {
                                                    UE_updatedPic = UE_Picture;
                                                    Log.e("if UE_Picture name is null:","onclick  :"+UE_Picture);
                                                }

                                                Log.e("final UE_selected_event :","onclick :"+UE_selected_event);
                                                Log.e("final UE_updatedPic :","onclick :"+UE_updatedPic);
                                                AddDecorationModel addDecorationModel1 =new AddDecorationModel(addDecID,vendorEmailAddress,UE_selected_event,UE_Decoration_name,UE_updatedPic,UE_Station_service,UE_Designers,UE_Stay_travel,UE_Event_start_price,UE_Event_upto_price);
                                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                                firebaseDatabase.getReference().child("AddDecoration").child(addDecID).setValue(addDecorationModel1);
                                                UE_prog_relay.setVisibility(View.GONE);
                                                UE_progressbar.setVisibility(View.GONE);
                                                Toast.makeText(MainActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();

                                            }catch (NullPointerException e){
                                                Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                                                UE_prog_relay.setVisibility(View.GONE);
                                                UE_progressbar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                                    firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                                    databaseReference.child("AddEvent").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            services = new ArrayList<String>();
                                            for(DataSnapshot ds : snapshot.getChildren()) {
                                                String spinnerName = ds.child("event_name").getValue(String.class);
                                                System.out.println("spinnerName is new : " + spinnerName);
                                                services.add(spinnerName);
                                            }

                                            adapters= new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, services);
                                            adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            UE_select_event.setAdapter(adapters);
                                            UE_select_event.getText().toString();
                                            UE_select_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    Toast.makeText(getApplicationContext(), "OnItemSelectedListener : " +  adapters.getItem(i).toString(), Toast.LENGTH_SHORT).show();
                                                    Update_select_event =  UE_select_event.getText().toString();

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onDeleteButtonClick(int position) {
                                    AddDecorationModel addDecorationModel = item.get(position);
                                    String addDecID = addDecorationModel.getAddDecorationID();
                                    String imgDecoration = addDecorationModel.getDecoration_name();
                                    Log.e("addDec id on delete :","onclick :"+addDecID);
                                    mDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("AddDecoration");
                                    mDatabase.child(addDecID).removeValue();
                                    Toast.makeText(MainActivity.this, "deleted item successfully ", Toast.LENGTH_SHORT).show();

                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    StorageReference desertRef = storageRef.child("decorationImages/"+imgDecoration+".jpg");

                                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MainActivity.this, "image is delete", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Uh-oh, an error occurred!
                                        }
                                    });
                                }
                            },item);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(eventAdapter);

                            UE_pic_image_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //check permission
                                    if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                            //permission not granted
                                            String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                                            //show pop up
                                            requestPermissions(permissions,PERMISSION_CODE);
                                        }
                                        else{
                                            //permission already granted
                                            pickImageFromGallary();
                                        }
                                    }
                                    else{
                                        //system os is less then
                                        pickImageFromGallary();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("userlist.321.........","onclick...:"+item);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void viewFeedbackMethod() {
        backdropLayout.close();
        setLayoutVisible(rl_view_feedback);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_dashboard);
        setLayoutInvisible(rl_view_ratings);
//                setLayoutInvisible(rl_view_customer_details);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Feedback_tbl");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                item_feedback =new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String feedback = ds.child("feedback").getValue(String.class);
                    String userEmail = ds.child("userEmail").getValue(String.class);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("UsersRegistration");
                    databaseReference.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                String fname = ds.child("fname").getValue(String.class);
                                String lname = ds.child("lname").getValue(String.class);
                                String mobile = ds.child("mobile").getValue(String.class);
                                String email = ds.child("email").getValue(String.class);

                                item_feedback.add(new FeedbackModel(fname + " " + lname, mobile,email, feedback));
                                recyclerView = findViewById(R.id.feedback_recycler);
                                feedbackAdapter = new FeedbackAdapter(getApplicationContext(), item_feedback,new ClickListener(){

                                    @Override
                                    public void onButtonClick(int position) {
                                        feedback_progressbar.setVisibility(View.GONE);
                                        feedback_prog_relay.setVisibility(View.GONE);
                                        backdropLayout.close();
                                        pageHistory.add(rl_feedback_view_details.getId());
                                        FeedbackViewDetailsId = rl_feedback_view_details.getId();
                                        setLayoutVisible(rl_feedback_view_details);
                                        setLayoutInvisible(rl_myProflie);
                                        setLayoutInvisible(rl_change_password);
                                        setLayoutInvisible(rl_add_decoration);
                                        setLayoutInvisible(rl_view_all_events);
                                        setLayoutInvisible(rl_view_orders);
                                        setLayoutInvisible(rl_dashboard);
                                        setLayoutInvisible(rl_view_ratings);
                                        setLayoutInvisible(rl_add_Event);
                                        setLayoutInvisible(rl_view_feedback);
                                        setLayoutInvisible(rl_otp_code_verification);
                                        setLayoutInvisible(rl_new_password);
                                        setLayoutInvisible(rl_password_updated);
                                        setLayoutInvisible(rl_single_view_order);
                                        setLayoutInvisible(rl_update_event);
                                        setLayoutInvisible(rl_edit_myprofile);

                                        FeedbackModel feedbackModel = item_feedback.get(position);
                                        String getCurrentUserEmail =feedbackModel.getUserEmail();
                                        Log.e("email is","onclick :"+getCurrentUserEmail);

                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Feedback_tbl");
                                        databaseReference.orderByChild("userEmail").equalTo(getCurrentUserEmail).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot ds : snapshot.getChildren()) {
                                                    String feedback = ds.child("feedback").getValue(String.class);
                                                    String accomodating = ds.child("accomodating").getValue(String.class);
                                                    String classyAndElegent = ds.child("classyAndElegent").getValue(String.class);
                                                    String grandDecor = ds.child("grandDecor").getValue(String.class);
                                                    String onTimeService = ds.child("onTimeService").getValue(String.class);
                                                    String professionalism = ds.child("professionalism").getValue(String.class);
                                                    String qualityOfWork = ds.child("qualityOfWork").getValue(String.class);
                                                    String uniqueIdea = ds.child("uniqueIdea").getValue(String.class);
                                                    String valueOfMoney = ds.child("valueOfMoney").getValue(String.class);

                                                    fb_view_feedback.setText(feedback);
//                                                    fb_view_feedback.setText("Feedback :"+System.getProperty("line.separator")+feedback);
                                                    fb_checkBox1.setChecked(Boolean.parseBoolean(accomodating));
                                                    fb_checkBox2.setChecked(Boolean.parseBoolean(classyAndElegent));
                                                    fb_checkBox3.setChecked(Boolean.parseBoolean(grandDecor));
                                                    fb_checkBox4.setChecked(Boolean.parseBoolean(onTimeService));
                                                    fb_checkBox5.setChecked(Boolean.parseBoolean(professionalism));
                                                    fb_checkBox6.setChecked(Boolean.parseBoolean(qualityOfWork));
                                                    fb_checkBox7.setChecked(Boolean.parseBoolean(uniqueIdea));
                                                    fb_checkBox8.setChecked(Boolean.parseBoolean(valueOfMoney));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(feedbackAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void viewRatingsMethod() {
        backdropLayout.close();
        setLayoutVisible(rl_view_ratings);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_view_orders);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_dashboard);
//                setLayoutInvisible(rl_view_customer_details);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Rating_tbl");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                item_rating =new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String rating = ds.child("rating").getValue(String.class);
                    String userEmail = ds.child("userEmail").getValue(String.class);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("UsersRegistration");
                    databaseReference.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                String fname = ds.child("fname").getValue(String.class);
                                String lname = ds.child("lname").getValue(String.class);
                                String mobile = ds.child("mobile").getValue(String.class);
                                String email = ds.child("email").getValue(String.class);

                                item_rating.add(new RatingModel(fname + " " + lname, mobile,email, rating));

                                recyclerView= findViewById(R.id.rating_recycler);
                                ratingAdapter = new RatingAdapter(getApplicationContext(),item_rating);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(ratingAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ViewOrdersMethod() {

        backdropLayout.close();
        setLayoutVisible(rl_view_orders);
        setLayoutInvisible(rl_myProflie);
        setLayoutInvisible(rl_change_password);
        setLayoutInvisible(rl_add_decoration);
        setLayoutInvisible(rl_view_all_events);
        setLayoutInvisible(rl_dashboard);
        setLayoutInvisible(rl_view_feedback);
        setLayoutInvisible(rl_view_ratings);
        setLayoutInvisible(rl_add_Event);
        setLayoutInvisible(rl_single_view_order);
        setLayoutInvisible(rl_otp_code_verification);
        setLayoutInvisible(rl_new_password);
        setLayoutInvisible(rl_password_updated);
        setLayoutInvisible(rl_update_event);
        setLayoutInvisible(rl_feedback_view_details);
        setLayoutInvisible(rl_edit_myprofile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("VendorRegistration").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Email = snapshot.child("email").getValue(String.class);
                vendorEmail = Email;
                Log.e("vendor Email 1 :","onclick :"+vendorEmail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
        databaseReference1.child("SetEventDetails").orderByChild("vendorEmail").equalTo(vendorEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewOrder_view_models =new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String isOrderConfirm = ds.child("isOrderConfirm").getValue(String.class);
                    Log.e(" isOrderConfirm","onclick :"+isOrderConfirm);

                        Log.e(" isOrderConfirm1", "onclick--------- :" + isOrderConfirm);
                        String addDecorationID = ds.child("addDecorationID").getValue(String.class);
                        String customerID = ds.child("customerID").getValue(String.class);
                        String meetingDate = ds.child("meetingDate").getValue(String.class);
                        String meetingDescription = ds.child("meetingDescription").getValue(String.class);
                        String meetingTime = ds.child("meetingTime").getValue(String.class);
                        String providePrice = ds.child("providePrice").getValue(String.class);
                        String eventDetailsID = ds.child("eventDetailsID").getValue(String.class);
                        Log.e("addDecorationID", "onclick :" + addDecorationID);
                        Log.e("customerID", "onclick :" + customerID);

                        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                        DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
                        databaseReference1.child("UsersRegistration").orderByChild("customerID").equalTo(customerID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String fname = ds.child("fname").getValue(String.class);
                                    String lname = ds.child("lname").getValue(String.class);
                                    String mobile = ds.child("mobile").getValue(String.class);
                                    String email = ds.child("email").getValue(String.class);
                                    String address = ds.child("address").getValue(String.class);

                                    FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                                    DatabaseReference databaseReference2 = firebaseDatabase2.getReference();
                                    databaseReference2.child("AddDecoration").orderByChild("addDecorationID").equalTo(addDecorationID).addValueEventListener(new ValueEventListener() {
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
                                                event_upto_price  = ds.child("event_upto_price").getValue(String.class);
                                                decoration_pic_image = ds.child("decoration_pic_image").getValue(String.class);
                                                Log.e("event_name new1","onclick:"+event_name);
                                                viewOrder_view_models.add(new ViewOrder_view_model(fname, lname, event_name, decoration_name, decoration_pic_image, mobile, email, address, providePrice, meetingDate, meetingTime, meetingDescription));
                                            }
                                            recyclerView = findViewById(R.id.voi_recycler);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                            FirebaseRecyclerOptions<ViewOrderItemModel> options =
                                                    new FirebaseRecyclerOptions.Builder<ViewOrderItemModel>()
                                                            .setQuery(FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("SetEventDetails").orderByChild("vendorEmail").equalTo(vendorEmail), ViewOrderItemModel.class)
                                                            .build();

                                            viewOrderItemAdapter = new ViewOrderItemAdapter(options, new ViewOrderItemAdapter.ClickListener() {
                                                @Override
                                                public void onButtonClick(int position) {
                                                    backdropLayout.close();
                                                    pageHistory.add(rl_single_view_order.getId());
                                                    ViewPageId = rl_single_view_order.getId();
                                                    setLayoutVisible(rl_single_view_order);
                                                    setLayoutInvisible(rl_myProflie);
                                                    setLayoutInvisible(rl_change_password);
                                                    setLayoutInvisible(rl_add_decoration);
                                                    setLayoutInvisible(rl_view_all_events);
                                                    setLayoutInvisible(rl_view_orders);
                                                    setLayoutInvisible(rl_dashboard);
                                                    setLayoutInvisible(rl_view_ratings);
                                                    setLayoutInvisible(rl_add_Event);
                                                    setLayoutInvisible(rl_view_feedback);
                                                    setLayoutInvisible(rl_otp_code_verification);
                                                    setLayoutInvisible(rl_new_password);
                                                    setLayoutInvisible(rl_password_updated);
                                                    setLayoutInvisible(rl_update_event);
                                                    setLayoutInvisible(rl_feedback_view_details);
                                                    setLayoutInvisible(rl_edit_myprofile);

                                                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                                    String get_fname = sharedpreferences.getString("fname ", "abc");
                                                    Log.e("get fname is: ","onclick:"+get_fname);
                                                    Log.e("position is :","onclick:"+position);

                                                    ViewOrder_view_model viewOrderItemModel = viewOrder_view_models.get(position);

                                                    svo_firstName.setText(viewOrderItemModel.getFname());
                                                    svo_lname.setText(viewOrderItemModel.getLname());
                                                    svo_eventName.setText(viewOrderItemModel.getEvent_name());
                                                    svo_decorationName.setText(viewOrderItemModel.getDecoration_name());
                                                    svo_price.setText(viewOrderItemModel.getProvidePrice());
                                                    svo_orderDate.setText(viewOrderItemModel.getMeetingDate());
                                                    svo_orderTime.setText(viewOrderItemModel.getMeetingTime());
                                                    svo_mobileNo.setText(viewOrderItemModel.getMobile());
                                                    svo_email.setText(viewOrderItemModel.getEmail());
                                                    svo_address.setText(viewOrderItemModel.getAddress());
                                                    svo_description.setText(viewOrderItemModel.getMeetingDescription());

                                                    String mBase64string = viewOrderItemModel.getDecoration_pic_image();
                                                    byte[] imageAsBytes = Base64.decode(mBase64string.getBytes(), Base64.DEFAULT);
                                                    svo_decorationImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));


                                                }
                                            }, getApplicationContext());
                                            recyclerView.setAdapter(viewOrderItemAdapter);

                                            viewOrderItemAdapter.startListening();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        viewOrderItemAdapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        viewOrderItemAdapter.stopListening();
//    }

    private void Google_signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
    }
    public void setLayoutInvisible(RelativeLayout layout) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
        }
    }
    public void setLayoutVisible(RelativeLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            layout.setClickable(true);
        }
    }
    public void setMainLayoutVisible(RelativeLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
//            layout.setClickable(false);
        }
    }

    int count = 0;
    @Override
    public void onBackPressed() {
        Log.e("onbackpress","onclick ");
        count++;

        for (int i = pageHistory.size() - 1; i >= 0; i--) {

            try {
                if (pageHistory.get(i - count).equals(dashboardPageId)) {
                    dashboardMethod();
                    break;
                }else if(pageHistory.get(i - count).equals(currentPageId)){
                    break;
                } else if (pageHistory.get(i - count).equals(myProfilePageId)) {
                    myProfileMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(changePassPageId)) {
                    changePassMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(addDecorationPageId)) {
                    addDecorationMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(addEventPageId)) {
                    addEventMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(viewAllEventsPageId)) {
                    viewAllEventsMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(viewOrdersPageId)) {
                    ViewOrdersMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(viewFeedbackPageId)) {
                    viewFeedbackMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(viewRatingsPageId)) {
                    viewRatingsMethod();
                    break;
                } else if (pageHistory.get(i).equals(ChangePasswordPageId)) {
                    ChangePasswordMethod();
                    break;
                } else if (pageHistory.get(i - count).equals(ViewPageId)) {
                    ViewOrdersMethod();
                    break;
                } else if (pageHistory.get(i).equals(UpdateEventId)) {
                    viewAllEventsMethod();
                    break;
                } else if (pageHistory.get(i).equals(editProfilePageId)) {
                    editProfile_method();
                    break;
                }else {
                    super.onBackPressed();
                }
            }catch (ArrayIndexOutOfBoundsException e){
//                    Toast.makeText(this, "Exception is: "+e, Toast.LENGTH_SHORT).show();
                super.onBackPressed();
            }
        }

    }

    private boolean validPhone_no(){

        String mobile_no = change_pass_phone.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(mobile_no.isEmpty()){
            change_pass_phone.setError("Field can't be empty");
            return false;
        }else if(!MOBILE_NO.matcher(mobile_no).matches()){
            change_pass_phone.setError("please check you are number.");
            return false;
        }else{
            change_pass_phone.setError(null);
            return true;
        }
    }



}
