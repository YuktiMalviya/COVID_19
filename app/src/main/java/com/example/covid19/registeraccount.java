package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registeraccount extends AppCompatActivity {

    EditText name,email,password;
    String uemail,uname,upwd;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeraccount);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.register_progress);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean n = validatename(name.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                validatemail(email.getText().toString().trim());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatepass(password.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public boolean validatemail(String mail){

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mail.isEmpty()){
            email.setError("Empty !!");
            email.requestFocus();
            return false;
        }

        if (!mail.matches(emailPattern)){
            email.setError("Enter valid email ");
            email.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validatepass(String pass){

        if (pass.isEmpty())
        {
            password.setError("Empty !!");
            password.requestFocus();
            return false;
        }

        if (pass.length()<6 || pass.length()>10){
            password.setError("Password length should be greater than 6 and less than 10");
            password.requestFocus();
            return false;
        }

        return true;
    }

    public boolean isNetworkAvailable(Context context) {

        if (context == null) return false;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ");
        return false;
    }

    public boolean validatename(String username){

        if (username.trim().isEmpty()){
            name.setError("Empty !!");
            name.requestFocus();
            return false;
        }
        if (username.length()>20){
            password.setError("Name length should be less than 20");
            password.requestFocus();
            return false;
        }
        return true;
    }

    public void registerbtn(View view) {

        uname = name.getText().toString().trim();
        uemail = email.getText().toString().trim();
        upwd = password.getText().toString().trim();

        if (validatemail(uemail) && validatename(uname) && validatepass(upwd)){
            if (isNetworkAvailable(this)){
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(uemail, upwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(uname).build();

                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    String uid =  mAuth.getUid();
                                                                    reference.child("accounts").child(uid).child("type").setValue("user");
                                                                    reference.child("users").child(uid).child("uid").setValue(uid);
                                                                    reference.child("users").child(uid).child("name").setValue(uname);
                                                                    reference.child("users").child(uid).child("email").setValue(email.getText().toString().trim());
                                                                    reference.child("users").child(uid).child("password").setValue(upwd);

                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    Toast.makeText(registeraccount.this, "Account Registered !", Toast.LENGTH_SHORT).show();
                                                                    Toast.makeText(registeraccount.this, "Please verify your email, to login in", Toast.LENGTH_LONG).show();
                                                                    Intent i = new Intent(registeraccount.this,loginaccount.class);
                                                                    startActivity(i);
                                                                    finish();
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(registeraccount.this, "User Cant't be Registered !", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                            else {
                                                Toast.makeText(registeraccount.this, "Can't verify your email id", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            progressBar.setVisibility(View.VISIBLE);

                                        }
                                    }, 2000);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(registeraccount.this, "Account Create failed due to ", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(registeraccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            }
        }

    }


    public void loginaccount(View view) {
        Intent intent = new Intent(registeraccount.this,loginaccount.class);
        startActivity(intent);
        finish();
    }
}
