package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter < ExampleAdapter.ExampleViewHolder > {

    private Context mcontext;
    private ArrayList<Cardlayout> mExampleList;
    private onItemClickListener mListener;

    public interface onItemClickListener {

        void onItemClick( int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public ExampleAdapter(Context context, ArrayList<Cardlayout> exampleList){
        mcontext = context;
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.card_layout, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Cardlayout currentItem = mExampleList.get(position);

        String state = currentItem.getmState();
        String confirmed = currentItem.getmconfirmed();
        String active = currentItem.getmactive();
        String recovered = currentItem.getmrecovered();
        String deaths = currentItem.getmdeaths();

        holder.mState.setText(state);
        holder.mConfiremed.setText(confirmed);
        holder.mActive.setText(active);
        holder.mRecovered.setText(recovered);
        holder.mDeaths.setText(deaths);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mState;
        public TextView mConfiremed;
        public TextView mActive;
        public TextView mRecovered;
        public TextView mDeaths;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            mState = itemView.findViewById(R.id.state);
            mConfiremed = itemView.findViewById(R.id.confirmed);
            mActive = itemView.findViewById(R.id.active);
            mRecovered = itemView.findViewById(R.id.recovered);
            mDeaths = itemView.findViewById(R.id.deaths);

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
