package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class users_list extends AppCompatActivity {

    private UserAdapter userAdapter;
    private List<User> mUsers;
    private RecyclerView recyclerView;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<String> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        recyclerView = findViewById(R.id.userrecyler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if (!dataSnapshot.hasChildren()){
                   Toast.makeText(users_list.this, "Nothing New Here, checkout after some time.", Toast.LENGTH_SHORT).show();
                   users_list.super.onBackPressed();
                   finish();
               }
               else {
                   usersList.clear();
                   for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                       Chat chat = snapshot.getValue(Chat.class);
                       if (chat.getSender().equals(firebaseUser.getUid())){
                           usersList.add(chat.getReceiver());
                       }
                       if (chat.getReceiver().equals(firebaseUser.getUid())){
                           usersList.add(chat.getSender());
                       }
                   }
                   ReadChats();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void ReadChats() {

        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    for (String id : usersList){

                        if (user.getUid().equals(id)){

                            if (mUsers.size()!=0){

                                for (User user1 : mUsers)
                                {
                                    if (!user.getUid().equals(user1.getUid())){

                                        mUsers.add(user);
                                    }
                                }
                            }
                            else {
                                mUsers.add(user);
                            }
                        }
                    }
                }

                userAdapter = new UserAdapter(getApplicationContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
