package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_profile extends AppCompatActivity {

    TextView profile_username,profile_email,profile_number;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    int TAKE_IMAGE_CODE = 10001;

    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profile_username = findViewById(R.id.profile_username);
        profile_email = findViewById(R.id.profile_email);
        profile_number = findViewById(R.id.profile_number);
        circleImageView = findViewById(R.id.user_profile_image);

        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser()!=null){

            profile_username.setText(firebaseAuth.getCurrentUser().getDisplayName().toString());
            profile_email.setText(firebaseAuth.getCurrentUser().getEmail().toString());

            DatabaseReference dbref = databaseReference.child(firebaseUser.getUid());
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("number")){
                       DatabaseReference userref = databaseReference.child(firebaseUser.getUid()).child("number");
                       userref.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               profile_number.setText(dataSnapshot.getValue().toString());
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (firebaseUser.getPhotoUrl()!=null){

                Glide.with(this)
                        .load(firebaseUser.getPhotoUrl())
                        .into(circleImageView);
            }

        }
        else {
            Toast.makeText(this, "Please Login !", Toast.LENGTH_SHORT).show();
        }

    }

    public void user_signout_btn(View view) {

        firebaseAuth.signOut();
        Intent intent = new Intent(User_profile.this,loginaccount.class);
        startActivity(intent);
        finish();
    }

    public void edit_profile(View view) {
        Intent intent = new Intent(User_profile.this,Edit_profile.class);
        startActivity(intent);
        finish();
    }
}
