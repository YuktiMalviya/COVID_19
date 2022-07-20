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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginaccount extends AppCompatActivity {

    EditText email,password;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference reference =  FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginaccount);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        progressBar = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();

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

    public void loginacc(View view) {

        String upwd,uemail;

        upwd = password.getText().toString().trim();
        uemail = email.getText().toString().trim();

        if (validatemail(uemail) && validatepass(upwd)){
            if (isNetworkAvailable(this)){
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(uemail, upwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //progressDialog.dismiss();

                                    getUserType();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.INVISIBLE);
                                    mAuth.signOut();
                                    Toast.makeText(loginaccount.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    Toast.makeText(loginaccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            }
            else {
                Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getUserType() {

        DatabaseReference userref = reference.child("accounts").child(mAuth.getUid()).child("type");
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.getValue(String.class);

                if (userType.equals("user")){

                    if (mAuth.getCurrentUser().isEmailVerified()){
                        //Toast.makeText(loginaccount.this, "Signed In Succesfully !", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(loginaccount.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        mAuth.signOut();
                        Toast.makeText(loginaccount.this, "Please verify your mail !", Toast.LENGTH_SHORT).show();
                    }

                }
                if (userType.equals("admin")){
                    progressBar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(loginaccount.this, "Admin Panel Coming Soon !", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(loginaccount.this, "Signed In Succesfully !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(loginaccount.this,AdminActivity.class);
                    startActivity(i);
                    finish();
                }

                if (userType.equals("doctor")){
                    progressBar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(loginaccount.this, "Doctor Panel Coming Soon !", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(loginaccount.this, "Signed In Succesfully !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(loginaccount.this,Doctor.class);
                    startActivity(i);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(loginaccount.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createacc(View view) {

        Intent intent = new Intent(loginaccount.this,registeraccount.class);
        startActivity(intent);
        finish();
    }

    public void forgetpass(View view) {
        //Toast.makeText(this, "Coming Soon !", Toast.LENGTH_SHORT).show();

        Intent i  =new Intent(loginaccount.this,forget_password.class);
        startActivity(i);
    }

}
