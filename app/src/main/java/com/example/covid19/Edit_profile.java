package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity {

    EditText profile_name,profile_email,profile_number;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    FirebaseUser firebaseUser;
    int TAKE_IMAGE_CODE = 10001;
    CircleImageView circleImageView;
    Bitmap bitmap;
    Button save_user_profile;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        circleImageView = findViewById(R.id.edit_user_profile_image);
        profile_name = findViewById(R.id.edit_user_name);
        profile_email = findViewById(R.id.edit_user_email);
        profile_number = findViewById(R.id.edit_phone_number);
        save_user_profile = findViewById(R.id.save_user_profile);


        save_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_edit_profile();
            }
        });
    }

    public void save_edit_profile() {

        save_user_profile.setEnabled(false);

        String name = profile_name.getText().toString().trim();
        String email = profile_email.getText().toString().trim();
        String number = profile_number.getText().toString().trim();

        if (validatename(name) && validatemail(email) && validatenumber(number)){
            update_user_profile(name,email,number);
        }

    }

    private void update_user_profile(String name, final String email, String number) {

        DatabaseReference ref = databaseReference.child(firebaseUser.getUid());

        dialog = ProgressDialog.show(Edit_profile.this, "",
                "Loading. Please wait...", true);

        ref.child("name").setValue(name);
        ref.child("email").setValue(email);
        ref.child("number").setValue(number);

        UserProfileChangeRequest upcr = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        firebaseUser.updateProfile(upcr).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(Edit_profile.this, "Please Verify Your Mail !", Toast.LENGTH_LONG).show();
                                        save_user_profile.setEnabled(true);
                                    }
                                },5000);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Edit_profile.this, "Failed !", Toast.LENGTH_SHORT).show();
                                save_user_profile.setEnabled(true);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_profile.this, "Failed !", Toast.LENGTH_SHORT).show();
                        save_user_profile.setEnabled(true);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit_profile.this, "Failed !", Toast.LENGTH_SHORT).show();
                save_user_profile.setEnabled(true);
            }
        });
    }

    public boolean validatename(String name){

        if (name.isEmpty()){
            profile_name.setError("Empty !!");
            profile_name.requestFocus();
            return false;
        }

        return true;
    }

    public boolean validatenumber(String number){

        if (number.isEmpty()){
            profile_number.setError("Empty !!");
            profile_number.requestFocus();
            return false;
        }

        if (number.length()<10){
            profile_number.setError("Invalid Number");
            profile_number.requestFocus();
            return false;
        }

        return true;
    }

    public boolean validatemail(String mail){

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mail.isEmpty()){
            profile_email.setError("Empty !!");
            profile_email.requestFocus();
            return false;
        }

        if (!mail.matches(emailPattern)){
            profile_email.setError("Enter valid email ");
            profile_email.requestFocus();
            return false;
        }
        return true;
    }

    public void add_user_profile_picture(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_IMAGE_CODE){
            switch (resultCode){
                case RESULT_OK:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    dialog = ProgressDialog.show(Edit_profile.this, "",
                            "Loading. Please wait...", true);
                    handleUpload(bitmap);
            }
        }

    }

    private void handleUpload(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(Uid + ".jpeg");

        reference.putBytes(byteArrayOutputStream.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadReference(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDownloadReference(StorageReference reference){

        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        setUserProfileUrl(uri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUserProfileUrl(Uri uri){

        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        circleImageView.setImageBitmap(bitmap);
                        dialog.dismiss();
                        Toast.makeText(Edit_profile.this, "Updated Profile Picture Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void reset_user_password(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_profile.this,R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("Are you Sure !");
        builder.setMessage("A Reset Email link will be send to your registered mail, click ok to proceed");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String emailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Edit_profile.this, "An email is sent with the link to reset your password !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Edit_profile.this, "Failed !", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
