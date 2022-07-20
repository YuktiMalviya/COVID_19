package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class selfassess extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfassess);

        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void symptomcheck(View view) {

        //Toast.makeText(this, "Coming Soon !", Toast.LENGTH_SHORT).show();
        if (isNetworkAvailable(this)){
            Intent i  = new Intent(selfassess.this,Symptomchecker.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultdoc(View view) {

        if (isNetworkAvailable(this)){
            Intent i = new Intent(selfassess.this,doctorlist.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "Coming Soon !", Toast.LENGTH_SHORT).show();
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

    public void spot_difference(View view) {
        Intent i = new Intent(selfassess.this,Difference.class);
        startActivity(i);
    }
}
