package com.mob3000.cinematrum.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReservationSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> data;

    public ReservationSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }

    @Override
    public int getCount() {
        return this.data.size() - 1;
    }

    public void updateData(ArrayList<String> newData){
        this.data = newData;
        clear();
        addAll(newData);
        this.notifyDataSetChanged();
        Log.d("RESERVATION ADAPTER", String.valueOf(this.data.size()));
        Log.d("RESERVATION ADAPTER", this.data.get(this.data.size() - 1));
    }
}
