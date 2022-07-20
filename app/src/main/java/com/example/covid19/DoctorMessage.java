package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorMessage extends AppCompatActivity {

    String uid = "";
    String sender = "";
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    EditText editText;
    FirebaseAuth mAuth;

    DatabaseReference database;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_message);

        editText = findViewById(R.id.chatmessage);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        sender = mAuth.getUid();

        Intent i = getIntent();
        uid = i.getStringExtra("uid");

        progressDialog = ProgressDialog.show(DoctorMessage.this,"Loading Messages ","Please Wait...", true);

//        ReadMessage(sender,uid);

        database = FirebaseDatabase.getInstance().getReference("users");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadMessage(sender,uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void sendMessage(String sender, String receiver, String message) {

        DatabaseReference schat = FirebaseDatabase.getInstance().getReference("chats");

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        schat.push().setValue(hashMap);

    }

    private void ReadMessage(final String myid, final String usser){

        DatabaseReference rchat = FirebaseDatabase.getInstance().getReference("chats");

        mChat = new ArrayList<>();

        if (isNetworkAvailable(this)){
            rchat.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mChat.clear();
                    if (!dataSnapshot.hasChildren())
                    {
                        progressDialog.dismiss();
                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        Chat chatt = snapshot.getValue(Chat.class);


                        if (chatt.getReceiver().equals(myid) && chatt.getSender().equals(usser) ||
                                chatt.getReceiver().equals(usser) && chatt.getSender().equals(myid)){

                            mChat.add(chatt);

                        }
                        progressDialog.dismiss();
                        messageAdapter = new MessageAdapter(DoctorMessage.this,mChat);
                        recyclerView.setAdapter(messageAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(DoctorMessage.this, "Cannot Retrieve Messages, Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DoctorMessage.this,selfassess.class);
                    startActivity(i);
                    finish();
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(DoctorMessage.this, "Cannot Retrieve Messages, Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DoctorMessage.this,selfassess.class);
            startActivity(i);
            finish();
        }

    }

    public void sendChatMessage(View view) {

        String msg = "";

        msg = editText.getText().toString();

        if (msg.isEmpty()){
            editText.setError("Type a message to send");
            editText.requestFocus();
        }
        else {
            if (isNetworkAvailable(this))
            {
                sendMessage(sender,uid,msg);
                editText.getText().clear();
            }
            else {
                Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        }

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

