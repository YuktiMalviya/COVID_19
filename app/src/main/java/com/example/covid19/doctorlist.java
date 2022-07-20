package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class doctorlist extends AppCompatActivity implements DoctorAdapter.onItemClickListener {

    RecyclerView recyclerView;
    DoctorAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference db;
    ArrayList<Doctorlistcard> doctorlistcard;
    private ProgressBar spinner;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorlist);

        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            finish();
        }

        spinner = (ProgressBar)findViewById(R.id.docprogress);

        db = FirebaseDatabase.getInstance().getReference("doctors");
        doctorlistcard = new ArrayList<>();
        recyclerView = findViewById(R.id.docrecyler);
        layoutManager = new LinearLayoutManager(this);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


       if (isNetworkAvailable(this)){
           db.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   if (!dataSnapshot.hasChildren()){

                       Toast.makeText(doctorlist.this, "No Doctors Found !", Toast.LENGTH_SHORT).show();
                       doctorlist.super.onBackPressed();
                       finish();

                   }
                   else {

                       for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                           Doctorfirebase doclist = snapshot.getValue(Doctorfirebase.class);
                           String name = doclist.getName();
                           String email = doclist.getEmail();
                           String uid = doclist.getUid();
                           doctorlistcard.add(new Doctorlistcard(name,email,uid));
                       }

                       adapter = new DoctorAdapter(doctorlist.this,doctorlistcard);
                       recyclerView.setAdapter(adapter);
                       adapter.setOnItemClickListener(doctorlist.this);

                       Handler handler = new Handler();
                       handler.postDelayed(new Runnable() {
                           @Override
                           public void run() {

                               spinner.setVisibility(View.GONE);
                               recyclerView.setVisibility(View.VISIBLE);
                           }
                       },1000);

                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   Toast.makeText(doctorlist.this, "Can't fetch Doctor List !", Toast.LENGTH_SHORT).show();
                   Intent i = new Intent(doctorlist.this,selfassess.class);
                   startActivity(i);
                   finish();
               }
           });
       }
       else {
           Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
           Intent i = new Intent(doctorlist.this,selfassess.class);
           startActivity(i);
           finish();
       }


    }

    @Override
    public void onItemClick(int position) {

        Doctorlistcard clickedItem = doctorlistcard.get(position);
        String uid = clickedItem.getmUid();
//        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(doctorlist.this,DoctorMessage.class);
        i.putExtra("uid",uid);
        startActivity(i);
        finish();
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
}
