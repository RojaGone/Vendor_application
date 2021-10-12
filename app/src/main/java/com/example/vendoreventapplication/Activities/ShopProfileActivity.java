package com.example.vendoreventapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vendoreventapplication.Models.GoogleUserModel;
import com.example.vendoreventapplication.Models.ShopProfile;
import com.example.vendoreventapplication.Models.User;
import com.example.vendoreventapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.util.regex.Pattern;

public class ShopProfileActivity extends AppCompatActivity {
    TextInputEditText signup_shopname,signup_shopaddress,
                      signup_city,signup_pincode,signup_shopphoneNo;
    TextView signup_opentime,signup_closetime;
    Button signup_shoplogo,register_btn;
    ImageView shoplogo_image;
    float v=0;

    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    RelativeLayout login_relative;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase,mDatabase1;
    private static final int  IMAGE_PIC_CODE=1000;
    private static final int PERMISSION_CODE=1000;
    private String abc = "";
    List<ShopProfile> shopProfiles;
    private StorageReference storageReference,storageReference1;

    public static final Pattern MOBILE_NO=Pattern.compile(
            //   "(?=.*[0-9])"+
            //  ".{10,}"
            "(0/91)?[7-9][0-9]{9}"
    );
    String picture_str = "";
    String recentlySelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        shopProfiles = new ArrayList<ShopProfile>();

        signup_shopname = findViewById(R.id.signup_shopname);
        signup_shopaddress = findViewById(R.id.signup_shopaddress);
        signup_city = findViewById(R.id.signup_city);
        signup_pincode = findViewById(R.id.signup_pincode);
        signup_shopphoneNo = findViewById(R.id.signup_shopphoneNo);
        signup_opentime = findViewById(R.id.signup_opentime);
        signup_closetime = findViewById(R.id.signup_closetime);
        signup_shoplogo = findViewById(R.id.signup_shoplogo);

        register_btn = findViewById(R.id.register_btn);
        shoplogo_image = findViewById(R.id.shoplogo_image);
        progressBar=findViewById(R.id.progressbar);
        relativeLayout=findViewById(R.id.prog_relay);
        login_relative=(RelativeLayout)findViewById(R.id.login_relative);

        progressBar.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);

        storageReference = FirebaseStorage.getInstance().getReference("ShopImages");
        storageReference1 = FirebaseStorage.getInstance().getReference("ImagesNew");
        mDatabase1 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("VendorRegistration");
        mDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/").getReference().child("ShopProfile");
        auth = FirebaseAuth.getInstance();

        login_relative.setTranslationZ(800);
        login_relative.setAlpha(v);
        login_relative.animate().translationZBy(0).alpha(1).setDuration(1000).setStartDelay(600).start();


        signup_opentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                Log.e("set time is :","onclick :"+minute);
                Log.e("set time is :","onclick :"+hour);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ShopProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        signup_opentime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time :");
                mTimePicker.show();
//                Toast.makeText(ShopProfileActivity.this, "hello set time", Toast.LENGTH_SHORT).show();
            }
        });

        signup_closetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                Log.e("set time is :","onclick :"+minute);
                Log.e("set time is :","onclick :"+hour);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ShopProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        signup_closetime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time :");
                mTimePicker.show();
//                Toast.makeText(ShopProfileActivity.this, "hello set time", Toast.LENGTH_SHORT).show();
            }
        });

        signup_shoplogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        pickImageFromGallary();
                    }
                }
                else{
                    pickImageFromGallary();
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_btn.setEnabled(false);
                String txt_shopname = signup_shopname.getText().toString();
//                String txt_shopemail = signup_shopemail.getText().toString();
                String txt_shopaddress = signup_shopaddress.getText().toString().trim();
                String txt_city = signup_city.getText().toString();
                String txt_pincode = signup_pincode.getText().toString();
                String txt_shopphoneNo = signup_shopphoneNo.getText().toString();
                String txt_opentime = signup_opentime.getText().toString();
                String txt_closetime = signup_closetime.getText().toString();
//                String txt_shoplogo = signup_shoplogo.getText().toString();


                    try {
                        String txt_picture = abc;
                     if (validPicture() == true || validShopName() == true || validpAddress() == true || validCity() == true || validPincode() == true || validPhoneNo() == true ){
                            registerUser(txt_shopname,txt_shopaddress,txt_city,txt_pincode, txt_shopphoneNo,txt_opentime,txt_closetime, txt_picture);

                        }
                    }catch (IllegalArgumentException e){
                        Log.e("User Exception reg1","Onclick"+e.getMessage());
                    }catch (Exception e){
                        Log.e("User Exception reg2","Onclick"+e.getMessage());
                    }
            }
        });
    }

    private boolean validPicture() {
        String txt_picture = abc;
        if(TextUtils.isEmpty(txt_picture)){
            Toast.makeText(ShopProfileActivity.this, "Empty credential!", Toast.LENGTH_SHORT).show();
            register_btn.setEnabled(true);
            return false;
        }
        return true;
    }

    private void registerUser(String txt_shopname,String txt_shopaddress,String txt_city,String txt_pincode, String txt_shopphoneNo,String txt_opentime,String txt_closetime,String txt_picture){
        String Fname=getIntent().getStringExtra("fname");
        String Lname=getIntent().getStringExtra("lname");
        String Email=getIntent().getStringExtra("email");
        String Address=getIntent().getStringExtra("address");
        String Mobile=getIntent().getStringExtra("mobile");
        String password=getIntent().getStringExtra("pass");
        String gendervalue=getIntent().getStringExtra("gender");

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(signInAccount != null){
            Uri targetUri = signInAccount.getPhotoUrl();
            String isConfirm = "done";
            String user_id = auth.getCurrentUser().getUid();
            Log.e("uid","onclick :"+auth.getCurrentUser().getUid());
            recentlySelectedImage = targetUri.toString();
            Log.e("recentlySelectedImage","onclick :"+recentlySelectedImage);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Calendar c = Calendar.getInstance();
            Log.e("calender c", "onClick: " + c );
            String date = sdf.format(c.getTime());
            Log.e("date", "onClick: " + date );

            GoogleUserModel googleUserModel = new GoogleUserModel(user_id,signInAccount.getGivenName(),signInAccount.getFamilyName(),signInAccount.getEmail(),recentlySelectedImage,isConfirm,date);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
            firebaseDatabase.getReference().child("VendorRegistration").child(user_id).setValue(googleUserModel);
            ShopProfile shopProfile = new ShopProfile(user_id, txt_shopname, txt_shopaddress, txt_city, txt_pincode, txt_shopphoneNo, txt_opentime, txt_closetime, txt_picture);
            mDatabase.child(user_id).setValue(shopProfile);
            Toast.makeText(ShopProfileActivity.this, "Registration successful....wait until Admin approval", Toast.LENGTH_SHORT).show();
            Log.e("after registration call intent new 0", "onclick");
            Intent intent = new Intent(ShopProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }else {

            String picture=getIntent().getStringExtra("pic");
            Uri myUri = Uri.parse(picture);
            InputStream imageStream = null;
            try {
                imageStream = this.getContentResolver().openInputStream(myUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            String forword_pic = encodeTobase64(yourSelectedImage);
            Log.e("the forword pic","onclick : "+forword_pic);
//            Bitmap immagex=yourSelectedImage;
//            File file= Environment.getExternalStorageDirectory();
//            File dir = new File(file.getAbsolutePath()+"/VendorImages/");
//            dir.mkdir();
//            File file1= new File(dir,System.currentTimeMillis()+".jpeg");
//            try{
//                outputStream= new FileOutputStream(file1);
//            }catch (FileNotFoundException e){
//                Log.e("exception","onclick :"+e.getMessage());
//                e.printStackTrace();
//            }
//            immagex.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            auth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener(ShopProfileActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                        Calendar c = Calendar.getInstance();
                        Log.e("calender c", "onClick: " + c );
                        String date = sdf.format(c.getTime());
                        Log.e("date", "onClick: " + date );

                        Uri myUri = Uri.parse(picture_str);
                        StorageReference filereference = storageReference.child(txt_shopname+"."+getFileExtension(myUri));
                        filereference.putFile(myUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String user_id = auth.getCurrentUser().getUid();
                                Log.e("user id  294","onclick :"+user_id);
                                String isConfirm = "done";


                                    User user = new User(user_id, Fname, Lname, Email, Address, Mobile, password, gendervalue, forword_pic, isConfirm, date);
                                    mDatabase1.child(user_id).setValue(user);
                                    ShopProfile shopProfile = new ShopProfile(user_id, txt_shopname, txt_shopaddress, txt_city, txt_pincode, txt_shopphoneNo, txt_opentime, txt_closetime, txt_picture);
                                    mDatabase.child(user_id).setValue(shopProfile);
                                    Toast.makeText(ShopProfileActivity.this, "Registration successful....wait until Admin approval", Toast.LENGTH_SHORT).show();
                                    Log.e("after registration call intent new 1", "onclick");
                                    Intent intent = new Intent(ShopProfileActivity.this, LoginActivity.class);
                                    startActivity(intent);

                            }
                        });

                    }
                }

            }).addOnFailureListener(ShopProfileActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("error....","onclick :"+e);
                    Toast.makeText(ShopProfileActivity.this, "Email adress is inavalid", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ShopProfileActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private boolean validPhoneNo() {
        register_btn.setEnabled(true);
        String txt_shopphoneNo = signup_shopphoneNo.getText().toString();
        String noWhiteSpace="(?=\\S+$)";
        if(txt_shopphoneNo.isEmpty()){
            signup_shopphoneNo.setError("Field can't be empty");
            return false;
        }else if(!MOBILE_NO.matcher(txt_shopphoneNo).matches()){
            signup_shopphoneNo.setError("please check you are phone number.");
            return false;
        }else{
            signup_shopphoneNo.setError(null);
            return true;
        }
    }

    private boolean validPincode() {
        register_btn.setEnabled(true);
        String txt_pincode = signup_pincode.getText().toString();
        String noWhiteSpace="(?=\\S+$)";
        if(txt_pincode.isEmpty()){
            signup_pincode.setError("Field can't be empty");
            return false;
        }else if(txt_pincode.length()>=7){
            signup_pincode.setError("please check you are pincode.");
            return false;
        }else{
            signup_pincode.setError(null);
            return true;
        }
    }

    private boolean validCity() {
        register_btn.setEnabled(true);
        String txt_city = signup_city.getText().toString();
        String noWhiteSpace="(?=\\S+$)";
        if(txt_city.isEmpty()){
            signup_city.setError("Field can't be empty");
            return false;
        }else if(txt_city.length()>=30){
            signup_city.setError("name is too long");
            return false;
        }else if(txt_city.matches(noWhiteSpace)){
            signup_city.setError("White space are not allowed.");
            return false;
        }else{
            signup_city.setError(null);
            return true;
        }
    }

    private boolean validpAddress() {
        register_btn.setEnabled(true);
        String txt_shopaddress = signup_shopaddress.getText().toString().trim();
        if(txt_shopaddress.isEmpty()){
            signup_shopaddress.setError("Field can't be empty");
            return false;
        }
        return true;
    }

    private boolean validShopName() {
        register_btn.setEnabled(true);
        String txt_shopname = signup_shopname.getText().toString().trim();
        String noWhiteSpace="(?=\\S+$)";
        if(txt_shopname.isEmpty()){
            signup_shopname.setError("Field can't be empty");
            return false;
        }else if(txt_shopname.length()>=50){
            signup_shopname.setError("name is too long");
            return false;
        }else if(txt_shopname.matches(noWhiteSpace)){
            signup_shopname.setError("White space are not allowed.");
            return false;
        }else{
            signup_shopname.setError(null);
        }
        return true;
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
                    Toast.makeText(ShopProfileActivity.this,"permission denied.....!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

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
        if (resultCode == RESULT_OK && requestCode == IMAGE_PIC_CODE)
            shoplogo_image.setImageURI(data.getData());
            picture_str = data.getData().toString();
            Uri targetUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = this.getContentResolver().openInputStream(targetUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            abc = encodeTobase64(yourSelectedImage);
            Log.e("abc is :","omclick :"+abc);

//            Bitmap immagex=yourSelectedImage;
//            File file= Environment.getExternalStorageDirectory();
//            File dir = new File(file.getAbsolutePath()+"/ShopImages/");
//            dir.mkdir();
//            File file1= new File(dir,System.currentTimeMillis()+".jpeg");
//            try{
//                outputStream= new FileOutputStream(file1);
//            }catch (FileNotFoundException e){
//                e.printStackTrace();
//            }
//            immagex.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onBackPressed() {
        Log.e("on click 11","onclick");
//        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//        finish();
        super.onBackPressed();
    }
}