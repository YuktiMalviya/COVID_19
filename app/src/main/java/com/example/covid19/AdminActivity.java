package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void addDoc(View view) {
        Intent i = new Intent(AdminActivity.this,AddDoctor.class);
        startActivity(i);
    }

    public void adminsignout(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            mAuth.signOut();
            Toast.makeText(this, "Signed Out ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminActivity.this,loginaccount.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}
