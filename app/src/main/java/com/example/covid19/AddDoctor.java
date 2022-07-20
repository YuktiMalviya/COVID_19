package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctor extends AppCompatActivity {

    EditText name,email,password;
    String uemail,uname,upwd;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    DatabaseReference uref,dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        name = findViewById(R.id.doctorname);
        email = findViewById(R.id.doctoremail);
        password = findViewById(R.id.doctorpassword);

        uref =  FirebaseDatabase.getInstance().getReference("accounts");
        dref =  FirebaseDatabase.getInstance().getReference("doctors");
        mAuth = FirebaseAuth.getInstance();

    }

    public boolean checkcred(){

        uname = name.getText().toString().trim();
        uemail = email.getText().toString().trim();
        upwd = password.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (uemail.isEmpty() || upwd.isEmpty() || uname.isEmpty()){


            if (uname.isEmpty())
            {
                name.setError("Empty !!");
                name.requestFocus();
                return false;
            }

            if (uemail.isEmpty())
            {
                email.setError("Empty !!");
                email.requestFocus();
                return false;
            }

            if (upwd.isEmpty())
            {
                password.setError("Empty !!");
                password.requestFocus();
                return false;
            }

        }

        if (!uemail.matches(emailPattern)){
            email.setError("Enter valid email ");
            email.requestFocus();
            return false;
        }
        if (upwd.length()<6){
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

    private boolean checknet() {

        if (isNetworkAvailable(this)){
            //Toast.makeText(this, "Connected !", Toast.LENGTH_SHORT).show();
           progressDialog = ProgressDialog.show(this,"Creating Account","Please Wait...", true);
            return true;
        }
        else{
            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void registerdoc(View view) {

        if (checkcred()){
            if (checknet()){

                mAuth.createUserWithEmailAndPassword(uemail,upwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            String uid = mAuth.getUid();
                            uref.child(uid).child("type").setValue("doctor");

                            dref.child(uid).child("uid").setValue(uid);
                            dref.child(uid).child("name").setValue(uname);
                            dref.child(uid).child("email").setValue(uemail);
                            dref.child(uid).child("password").setValue(upwd);

                            progressDialog.dismiss();
                            Toast.makeText(AddDoctor.this, "Doctor Registered", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(AddDoctor.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }
    }
}
