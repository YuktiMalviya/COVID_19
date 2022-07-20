package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mcontext;
    List<Chat> mChat;
    private DoctorAdapter.onItemClickListener mListener;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    FirebaseAuth firebaseUser;

    public MessageAdapter(Context context, List<Chat> Chat){
        mcontext = context;
        mChat = Chat;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right_item,parent,false);
            return new MessageAdapter.MessageViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left_item,parent,false);
            return new MessageAdapter.MessageViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        Chat chat  = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView show_message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance();


        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){  // firebaseUser.getUid()
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }
}
