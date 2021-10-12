package com.example.vendoreventapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendoreventapplication.Models.User;
import com.example.vendoreventapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    TextInputEditText signup_firstname,signup_lastname,signup_email,signup_address,signup_phoneNo,signup_password,signup_confirm_password;
    Button signup_profile_btn,signup_btn;
    ImageView profile_image;
    RadioGroup signup_gender;
    RelativeLayout login_relative;
    TextView alreadySignUpUser;
    float v=0;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private static final int  IMAGE_PIC_CODE=1000;
    private static final int PERMISSION_CODE=1000;
    private String pic = "";
    List<User> users;
    private StorageReference storageReference;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    public static final Pattern PASSWORD_PATTERN=Pattern.compile("^"+
            "(?=.*[@#$%^&*+=])"+
            "(?=\\S+$)"+    //no white space
            ".{6,}"+
            "$");


    public static final Pattern MOBILE_NO=Pattern.compile(
            //   "(?=.*[0-9])"+
            //  ".{10,}"
            "(0/91)?[5-9][0-9]{9}"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        users = new ArrayList<User>();
        signup_firstname = findViewById(R.id.signup_firstname);
        signup_lastname = findViewById(R.id.signup_lastname);
        signup_email = findViewById(R.id.signup_email);
        signup_address = findViewById(R.id.signup_address);
        signup_phoneNo = findViewById(R.id.signup_phoneNo);
        signup_password = findViewById(R.id.signup_password);
        signup_confirm_password = findViewById(R.id.signup_confirm_password);
        signup_gender = findViewById(R.id.signup_gender);
        profile_image = findViewById(R.id.profile_image);
        signup_profile_btn = findViewById(R.id.signup_profile_btn);
        signup_btn = findViewById(R.id.signup_btn);
        progressBar=findViewById(R.id.progressbar);
        relativeLayout=findViewById(R.id.prog_relay);
        login_relative=(RelativeLayout)findViewById(R.id.login_relative);
        alreadySignUpUser = (TextView)findViewById(R.id.alreadySignUpUser);

        progressBar.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);

        storageReference = FirebaseStorage.getInstance().getReference("VendorImages");
        mDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("VendorRegistration");
        auth = FirebaseAuth.getInstance();

        login_relative.setTranslationZ(800);
        login_relative.setAlpha(v);
        login_relative.animate().translationZBy(0).alpha(1).setDuration(1000).setStartDelay(600).start();

        alreadySignUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_btn.setEnabled(false);
                String txt_Fname = signup_firstname.getText().toString();
                String txt_Lname = signup_lastname.getText().toString();
                String txt_Email = signup_email.getText().toString().trim();
                String txt_Address = signup_address.getText().toString();
                String txt_Mobile = signup_phoneNo.getText().toString();
                String txt_Password = signup_password.getText().toString();
                String txt_Confirm_password = signup_confirm_password.getText().toString();
                RadioButton checkedBtn = findViewById(signup_gender.getCheckedRadioButtonId());

                try {
                    final String txt_gendervalue = checkedBtn.getText().toString();
                    String txt_picture = pic;
                    Log.e("txt_picture","onclick :"+txt_picture);

                    if (TextUtils.isEmpty(txt_gendervalue) || TextUtils.isEmpty(txt_picture)) {
                        Toast.makeText(RegistrationActivity.this, "Empty credential!", Toast.LENGTH_SHORT).show();
                        signup_btn.setEnabled(true);

                    } else if (validFirstname() == false) {

                    } else if (validLastname() == false) {

                    } else if (validateEmail() == false) {

                    } else if (validAddress() == false) {

                    } else if (validMobile_no() == false) {

                    } else if (validatePassword() == false) {

                    } else if (validConfirm_pass() == false) {

                    } else {
                        registerUser(txt_Fname, txt_Lname, txt_Email, txt_Address, txt_Mobile, txt_Password, txt_gendervalue, txt_picture);
                    }
                }catch (NullPointerException e){
                    Toast.makeText(RegistrationActivity.this, "Field can not be empty", Toast.LENGTH_SHORT).show();
                    signup_btn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });

        signup_profile_btn.setOnClickListener(new View.OnClickListener() {
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
                    try{
                        pickImageFromGallary();
                    }catch (RuntimeException e){

                    }
                }
                else{
                    Toast.makeText(RegistrationActivity.this,"permission denied.....!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PIC_CODE)
            profile_image.setImageURI(data.getData());
        try {
            pic = data.getData().toString();
        }catch (NullPointerException e){
            Log.e("exception in registration activity","onclick :"+e);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void registerUser(String Fname,String Lname,String Email,String Address,String Mobile, String password,String gendervalue,String picture){

                            progressBar.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.VISIBLE);

                            try{
                                Uri myUri = Uri.parse(picture);
                                StorageReference filereference = storageReference.child(Fname+"."+getFileExtension(myUri));
                                filereference.putFile(myUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////
                                        Intent intent = new Intent(RegistrationActivity.this, ShopProfileActivity.class);
                                        intent.putExtra("fname",Fname);
                                        intent.putExtra("lname",Lname);
                                        intent.putExtra("email",Email);
                                        intent.putExtra("address",Address);
                                        intent.putExtra("mobile",Mobile);
                                        intent.putExtra("pass",password);
                                        intent.putExtra("gender",gendervalue);
                                        Log.e("picture_reg","onclcik :"+gendervalue);
                                        intent.putExtra("pic",picture);
                                        Log.e("picture","onclcik :"+picture);
                                        startActivity(intent);
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                        relativeLayout.setVisibility(View.GONE);
                                    }
                                });
                            }catch (IllegalArgumentException e){
                                Log.e("User Exception re3","Onclick"+e.getMessage());

                            }



    }

    private boolean validateEmail(){
        signup_btn.setEnabled(true);
        String email=signup_email.getText().toString().trim();

        if(email.isEmpty()){
            signup_email.setError("Field can't be empty");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signup_email.setError("please enter a valid email address");
            return false;
        }
        else{
            signup_email.setError(null);
            return true;
        }
    }


    private boolean validFirstname(){
        signup_btn.setEnabled(true);
        String fname = signup_firstname.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(fname.isEmpty()){
            signup_firstname.setError("Field can't be empty");
            return false;
        }else if(fname.length()>=15){
            signup_firstname.setError("name is too long");
            return false;
        }else if(fname.matches(noWhiteSpace)){
            signup_firstname.setError("White space are not allowed.");
            return false;
        }else{
            signup_firstname.setError(null);
            return true;
        }
    }

    private boolean validLastname(){
        signup_btn.setEnabled(true);
        String lname = signup_lastname.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(lname.isEmpty()){
            signup_lastname.setError("Field can't be empty");
            return false;
        }else if(lname.length()>=15){
            signup_lastname.setError("name is too long");
            return false;
        }else if(lname.matches(noWhiteSpace)){
            signup_lastname.setError("White space are not allowed.");
            return false;
        }else{
            signup_lastname.setError(null);
            return true;
        }
    }



    private boolean validAddress(){
        signup_btn.setEnabled(true);
        String address= signup_address.getText().toString().trim();
        if(address.isEmpty()){
            signup_address.setError("Field can't be empty");
            return false;
        }
        return true;
    }

    private boolean validMobile_no(){
        signup_btn.setEnabled(true);
        String mobile_no = signup_phoneNo.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(mobile_no.isEmpty()){
            signup_phoneNo.setError("Field can't be empty");
            return false;
        }else if(!MOBILE_NO.matcher(mobile_no).matches()){
            signup_phoneNo.setError("please check you are number.");
            return false;
        }else{
            signup_phoneNo.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        signup_btn.setEnabled(true);
        String password=signup_password.getText().toString().trim();

        if(password.isEmpty()){
            signup_password.setError("Field can't be empty");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(password).matches()){
            signup_password.setError("please use special charecters and Do not use white space and it shold be 6 charecters. Ex:abc@123");
            return false;
        }
        else{
            signup_password.setError(null);
            return true;
        }
    }
    private boolean validConfirm_pass(){
        signup_btn.setEnabled(true);
        String password = signup_password.getText().toString().trim();
        String confirm_pass = signup_confirm_password.getText().toString().trim();

        if(confirm_pass.isEmpty()){
            signup_confirm_password.setError("Field can't be empty");
            return false;
        }else if(!password.equals(confirm_pass)){
            signup_confirm_password.setError("Password Not matching");
            return false;
        }else{
            signup_confirm_password.setError(null);
            return true;
        }
    }

}