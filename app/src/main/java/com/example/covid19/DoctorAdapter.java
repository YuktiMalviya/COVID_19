package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private Context mcontext;
    ArrayList<Doctorlistcard> mdoctorlistcard;
    private onItemClickListener mListener;

    public interface onItemClickListener {

        void onItemClick( int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doclistcard,parent,false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {

        Doctorlistcard currentItem = mdoctorlistcard.get(position);
        holder.tvdocname.setText(currentItem.getmName());

    }

    @Override
    public int getItemCount() {
        return mdoctorlistcard.size();
    }

    public DoctorAdapter(Context context, ArrayList<Doctorlistcard> doctorlistcard){
        mcontext = context;
        mdoctorlistcard = doctorlistcard;
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder{

        TextView tvdocname;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvdocname = itemView.findViewById(R.id.docname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }

                    }
                }
            });

        }
    }

}
