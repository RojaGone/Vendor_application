package com.example.vendoreventapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.vendoreventapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout login_relative,login_forgot_password;
    float v=0;
    Button login_btn,forgot_pass_btn;
    TextView newUser,forgot_pass;
    TextInputEditText login_email,login_password;
    ImageView fab_google;
    private TextInputEditText change_pass_phone,fp_email;
    private PinView pin_num_view;

    FirebaseAuth firebaseAuth;
    String userID;
    FirebaseAuth auth;
    FirebaseUser users;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 235;
    GoogleSignInClient mGoogleSignInClient;

    public static final Pattern MOBILE_NO=Pattern.compile(
            //   "(?=.*[0-9])"+
            //  ".{10,}"
            "(0/91)?[5-9][0-9]{9}"
    );
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    int googleLoginCount =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login_relative=(RelativeLayout)findViewById(R.id.login_relative);
        login_btn=(Button)findViewById(R.id.login_btn);
        newUser=(TextView)findViewById(R.id.newUser);
        login_email = (TextInputEditText)findViewById(R.id.login_email);
        login_password = (TextInputEditText)findViewById(R.id.login_password);
        fab_google = (ImageView) findViewById(R.id.fab_google);
        progressBar=findViewById(R.id.progressbar);
        relativeLayout=findViewById(R.id.prog_relay);
        forgot_pass = findViewById(R.id.forgot_pass);
        login_forgot_password = findViewById(R.id.login_forgot_password);
        change_pass_phone =(TextInputEditText)findViewById(R.id.change_pass_phone);
        pin_num_view = (PinView)findViewById(R.id.pin_num_view);
        forgot_pass_btn = findViewById(R.id.forgot_pass_btn);
        fp_email = findViewById(R.id.fp_email);

        progressBar.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);

        firebaseAuth =FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();

        mAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fragmentManager = getSupportFragmentManager();

        login_forgot_password.setVisibility(View.GONE);
        login_relative.setVisibility(View.VISIBLE);

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 1;
                login_relative.setVisibility(View.GONE);
                newUser.setVisibility(View.GONE);
                login_forgot_password.setVisibility(View.VISIBLE);
                Log.e("done","onclick");
                forgot_pass_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txt_email = fp_email.getText().toString();
                        if(validateEmail() == true) {

                            firebaseAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        fp_email.setText("");
                                        Toast.makeText(LoginActivity.this, "Password send to your email....please check your email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        fp_email.setText("");
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_email.setEnabled(false);
                login_password.setEnabled(false);
                login_btn.setEnabled(false);
                fab_google.setEnabled(false);
                forgot_pass.setEnabled(false);
                newUser.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);

                String txt_email = login_email.getText().toString();
                String txt_password = login_password.getText().toString();
                Log.e("email....", "onclick :  " + txt_email);
                Log.e("pass....", "onclick :  " + txt_password);

//                FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
//                DatabaseReference databaseReference2 = firebaseDatabase2.getReference();
//                databaseReference2.child("VendorRegistration").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            String email = ds.child("email").getValue(String.class);
//                            Log.e("email.... out", "onclick :" + email);
//                            if(email.equals(txt_email)){
//                                String isConfirm = ds.child("isConfirm").getValue(String.class);
//                                Log.e("isConfirm ", "onclick :" + isConfirm);
//                                Log.e("email.... ", "onclick :" + email);
//
//                                String bool = "true";
//                                if(isConfirm.equals("true")) {
//                                    Log.e("equal.... ", "onclick :" );
//                                    if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
//                                        Toast.makeText(LoginActivity.this, "Empty credential!", Toast.LENGTH_SHORT).show();
//                                        login_btn.setEnabled(true);
//
//                                    } else if (txt_email.equals(email)){
//                                        loginUser(txt_email, txt_password);
//                                    } else {
//                                        login_btn.setEnabled(true);
//                                        login_email.setError("this email is not exit");
//                                        progressBar.setVisibility(View.GONE);
//                                        relativeLayout.setVisibility(View.GONE);
//                                        login_email.setText("");
//                                        login_password.setText("");
//                                        Toast.makeText(LoginActivity.this, "this email is not exit", Toast.LENGTH_SHORT).show();
//                                    }
//                                }else {
//                                    progressBar.setVisibility(View.GONE);
//                                    relativeLayout.setVisibility(View.GONE);
//                                    login_email.setEnabled(true);
//                                    login_password.setEnabled(true);
//                                    login_btn.setEnabled(true);
//                                    fab_google.setEnabled(true);
//                                    forgot_pass.setEnabled(true);
//                                    newUser.setEnabled(true);
//                                    login_email.setText("");
//                                    login_password.setText("");
//                                    login_btn.setEnabled(true);
//                                    Toast.makeText(LoginActivity.this, "Wait until Admin approval....", Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                progressBar.setVisibility(View.GONE);
//                                relativeLayout.setVisibility(View.GONE);
//                                login_email.setEnabled(true);
//                                login_password.setEnabled(true);
//                                login_btn.setEnabled(true);
//                                fab_google.setEnabled(true);
//                                forgot_pass.setEnabled(true);
//                                newUser.setEnabled(true);
//                                login_email.setText("");
//                                login_password.setText("");
//                                login_btn.setEnabled(true);
//                                Toast.makeText(LoginActivity.this, "email is not exists....", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                DatabaseReference databaseReference2 = firebaseDatabase2.getReference();
                databaseReference2.child("VendorRegistration").orderByChild("email").equalTo(txt_email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String isConfirm = ds.child("isConfirm").getValue(String.class);
                            Log.e("isConfirm ", "onclick :" + isConfirm);
                            String email = ds.child("email").getValue(String.class);
                            Log.e("email ", "onclick :" + email);

                            String bool = "true";
                            if(isConfirm.equals(bool)) {

                                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                                    Toast.makeText(LoginActivity.this, "Empty credential!", Toast.LENGTH_SHORT).show();
                                    login_btn.setEnabled(true);

                                } else if (txt_email.equals(email)){
                                    loginUser(txt_email, txt_password);
                                } else {
                                    login_btn.setEnabled(true);
                                    login_email.setError("this email is not exit");
                                    progressBar.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.GONE);
                                    login_email.setText("");
                                    login_password.setText("");
                                    Toast.makeText(LoginActivity.this, "this email is not exit", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                progressBar.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.GONE);
                                login_email.setEnabled(true);
                                login_password.setEnabled(true);
                                login_btn.setEnabled(true);
                                fab_google.setEnabled(true);
                                forgot_pass.setEnabled(true);
                                newUser.setEnabled(true);
                                login_email.setText("");
                                login_password.setText("");
                                login_btn.setEnabled(true);
                                Toast.makeText(LoginActivity.this, "Wait until Admin approval....", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("exception","onclick");
                    }
                });

            }
        });
        fab_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        login_relative.setTranslationZ(800);
        login_relative.setAlpha(v);
        login_relative.animate().translationZBy(0).alpha(1).setDuration(1000).setStartDelay(600).start();

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

    private boolean validateEmail(){

        String email=fp_email.getText().toString();
        Log.e("forgot email","onclick :"+email);

        if(email.isEmpty()){
            fp_email.setError("Field can't be empty");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            fp_email.setError("please enter a valid email address");
            return false;
        }
        else{
            fp_email.setError(null);
            return true;
        }
    }

    public void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    users =  auth.getCurrentUser();
                    userID =users.getUid();
                    FirebaseUser user =firebaseAuth.getCurrentUser();
                    Log.e("user Uid....","onclick :  "+user.getUid());
                    Log.e("user Email....","onclick :  "+user.getEmail());

                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(),userPageActivity.class));
//                    finish();
                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                    Log.e("Uid is ...login","onClick :"+userID);
                    intent.putExtra("Uid",user.getUid());
                    startActivity(intent);
                    finish();
                }else{
                    login_btn.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                login_email.setEnabled(true);
                login_password.setEnabled(true);
                login_btn.setEnabled(true);
                fab_google.setEnabled(true);
                forgot_pass.setEnabled(true);
                newUser.setEnabled(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // malli malli login cheyalsina avasram ledhu e code direct ga main page ni open chesthadi
        Log.e("current user","onclick"+mAuth.getCurrentUser());
        if(mAuth.getCurrentUser() != null){
            finish();
            Intent intent= new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("Uid",userID);
            startActivity(intent);
            finish();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // malli malli login cheyalsina avasram ledhu e code direct ga main page ni open chesthadi
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount != null){
//            FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
//            DatabaseReference databaseReference2 = firebaseDatabase2.getReference();
//            databaseReference2.child("VendorRegistration").orderByChild("id").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot ds : snapshot.getChildren()) {
//                        progressBar.setVisibility(View.GONE);
//                        relativeLayout.setVisibility(View.GONE);
//
//                        String isConfirm = ds.child("isConfirm").getValue(String.class);
//                        Log.e("isconfirm in onstart", "onclick: " + isConfirm);
//                        String bool = "true";
//                        if (isConfirm.equals(bool)) {
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.putExtra("Uid", mAuth.getCurrentUser().getUid());
//                            startActivity(intent);
//                            finish();
//                        }
////                        else {
////                            progressBar.setVisibility(View.GONE);
////                            relativeLayout.setVisibility(View.GONE);
////                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
////                            startActivity(intent);
////                            finish();
////                            Toast.makeText(LoginActivity.this, "Wait until Admin approval....", Toast.LENGTH_SHORT).show();
////                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }else {
//            if (mAuth.getCurrentUser() != null) {
//                login_email.setEnabled(false);
//                login_password.setEnabled(false);
//                login_btn.setEnabled(false);
//                fab_google.setEnabled(false);
//                forgot_pass.setEnabled(false);
//                newUser.setEnabled(false);
//                progressBar.setVisibility(View.VISIBLE);
//                relativeLayout.setVisibility(View.VISIBLE);
//
//                FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
//                DatabaseReference databaseReference2 = firebaseDatabase2.getReference();
//                databaseReference2.child("VendorRegistration").orderByChild("id").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            progressBar.setVisibility(View.GONE);
//                            relativeLayout.setVisibility(View.GONE);
//
//                            String isConfirm = ds.child("isConfirm").getValue(String.class);
//                            Log.e("isconfirm in onstart", "onclick" + isConfirm);
//                            String bool = "true";
//                            if (isConfirm.equals(bool)) {
//                                FirebaseUser user = firebaseAuth.getCurrentUser();
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("Uid", mAuth.getCurrentUser().getUid());
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                progressBar.setVisibility(View.GONE);
//                                relativeLayout.setVisibility(View.GONE);
//                                login_email.setEnabled(true);
//                                login_password.setEnabled(true);
//                                login_btn.setEnabled(true);
//                                fab_google.setEnabled(true);
//                                forgot_pass.setEnabled(true);
//                                newUser.setEnabled(true);
//                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                                Toast.makeText(LoginActivity.this, "Wait until Admin approval....", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        }
////        else{
////
////            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
////            startActivity(intent);
////            finish();
////            login_btn.setEnabled(true);
////        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("TAG3", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("TAG4", "Google sign in failed", e);
                // ...
                progressBar.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
//                Toast.makeText(getActivity(), "error is" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            Log.e("TAG1", "signInWithCredential:success"+signInAccount.getEmail());
                            Log.e("TAG2", "signInWithCredential:success"+signInAccount.getDisplayName());
                            Log.e("TAG3", "signInWithCredential:success"+signInAccount.getFamilyName());
                            Log.e("TAG4", "signInWithCredential:success"+signInAccount.getGivenName());
                            Log.e("TAG5", "signInWithCredential:success"+signInAccount.getPhotoUrl());
                            Log.e("TAG6", "signInWithCredential:success"+signInAccount.getId());

                            Log.e("uid", "onclick :" + mAuth.getCurrentUser().getUid());
                            String Uids = mAuth.getCurrentUser().getUid();

                            FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance("https://vendoreventapplication-default-rtdb.firebaseio.com/");
                            DatabaseReference databaseReference2 = firebaseDatabase2.getReference();
                            databaseReference2.child("VendorRegistration").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String email = ds.child("email").getValue(String.class);
                                        Log.e("email in onstart", "onclick :" + email);

                                        if (email.equals(Uids)) {
                                            int googleLoginCount = 0;

                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, ShopProfileActivity.class);
                                            Log.e("Uid is ...", "onClick :" + userID);
                                            intent.putExtra("Uid", userID);
                                            startActivity(intent);
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("exceptin1","onclick");
                                }
                            });

//                            Log.e("sgsdgf","onclick"+googleLoginCount);
//                            if(googleLoginCount != 0) {
                                databaseReference2.child("VendorRegistration").orderByChild("id").equalTo(Uids).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            googleLoginCount++;
                                            String email = ds.child("email").getValue(String.class);
                                            Log.e("email in onstart11", "onclick :" + email);
                                            String isConfirm = ds.child("isConfirm").getValue(String.class);
                                            String bool = "true";
                                            if (isConfirm.equals(bool)) {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra("Uid", mAuth.getCurrentUser().getUid());
                                                startActivity(intent);
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(LoginActivity.this, "wait until vendor accepted", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                                                Log.e("Uid is1 ...", "onClick :" + userID);
//                                                intent.putExtra("Uid", userID);
//                                                startActivity(intent);
//                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("exceptin", "onclick");
                                    }
                                });
//                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("TAG2", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    private void signIn() {
        Log.e("TAG0", "abc");
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.e("TAG01", "def");
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.e("TAG02", "ghi");
    }

    int count = 0;

    @Override
    public void onBackPressed() {
//        Fragment fragment = fragmentManager.findFragmentById(R.id.login_relative);
//        Log.e("fragment.. ","onlcick :"+fragment);
//        if(fragment != null){
//            transaction= fragmentManager.beginTransaction();
//            transaction.remove(fragment);
//            transaction.commit();
//        }
//        else {
//            super.onBackPressed();
//        }
        Log.e("count","onclick :"+count);
        if(count == 1) {
            login_forgot_password.setVisibility(View.GONE);
            login_relative.setVisibility(View.VISIBLE);
            newUser.setVisibility(View.VISIBLE);
            count = 0;
        }else {
            super.onBackPressed();
        }
    }
}