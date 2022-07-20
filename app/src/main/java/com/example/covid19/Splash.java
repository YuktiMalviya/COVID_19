package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splashprogress);

        mAuth = FirebaseAuth.getInstance();

//        mAuth.signOut();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.VISIBLE);

            }
        }, 1000);

        if (!isNetworkAvailable(this)){
            No_Internet_Diaglog();
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mAuth.getCurrentUser() != null) {
                        getUserType();
                    }
                    else {
                        Intent i = new Intent(Splash.this,loginaccount.class);
                        startActivity(i);
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                }
            }, 2000);
        }

    }

    private void No_Internet_Diaglog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this,R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("No Internet !");
        final View customLayout = getLayoutInflater().inflate(R.layout.no_internet,null,false);
        builder.setView(customLayout);

        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (alertInternet()){
                    getUserType();
                }
                else {
                    Toast.makeText(Splash.this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
                    No_Internet_Diaglog();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean alertInternet(){
        if (isNetworkAvailable(this)){return true;}
        else {return false;}
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

    private void getUserType() {

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("accounts").child(mAuth.getUid()).child("type");
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.getValue(String.class);

                if (userType.equals("user")){
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(Splash.this, "Signed In Succesfully !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Splash.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                if (userType.equals("admin")){
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(Splash.this, "Signed In Succesfully !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Splash.this,AdminActivity.class);
                    startActivity(i);
                    finish();
                }

                if (userType.equals("doctor")){
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(Splash.this, "Signed In Succesfully !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Splash.this,Doctor.class);
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
}
