package com.example.covid19;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUser;

    public UserAdapter(Context mContext, List<User> mUser){
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = mUser.get(position);
        holder.username.setText(user.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getUid();
                Toast.makeText(mContext, userId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,DoctorMessage.class);
                intent.putExtra("uid", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
        }
    }
}
