package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<CustomItemSpinner> {

    public CustomSpinnerAdapter(@NonNull Context context, ArrayList<CustomItemSpinner> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    public View customView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_state,parent,false);
        }
        CustomItemSpinner items = getItem(position);
        TextView spinnerName = convertView.findViewById(R.id.tvCustomSpinner);
        if (items!=null){
            spinnerName.setText(items.getSpinnerText());
        }
        return convertView;
    }
}
