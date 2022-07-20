package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forget_password extends AppCompatActivity {

    EditText forget_password;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forget_password = findViewById(R.id.forget_email);
        progressBar = findViewById(R.id.forget_progress);

        mAuth = FirebaseAuth.getInstance();

        forget_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatemail(forget_password.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean validatemail(String mail){

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mail.isEmpty()){
            forget_password.setError("Empty !!");
            forget_password.requestFocus();
            return false;
        }

        if (!mail.matches(emailPattern)){
            forget_password.setError("Enter valid email ");
            forget_password.requestFocus();
            return false;
        }
        return true;
    }

    public void forgetpassword_btn(View view) {
        if (validatemail(forget_password.getText().toString().trim())){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(forget_password.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(forget_password.this, "Email Send !", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(forget_password.this, "Email Address Can't be Authenticated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Toast.makeText(this, "Invalid Email Address format!", Toast.LENGTH_SHORT).show();
        }
    }
}
