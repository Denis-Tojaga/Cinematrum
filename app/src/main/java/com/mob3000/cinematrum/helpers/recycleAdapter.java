package com.mob3000.cinematrum.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.MyViewHolder> {

    private ArrayList<Movie> tryArrayList;


    public recycleAdapter(ArrayList<Movie> tryArrayList) {
        this.tryArrayList = tryArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String picture = tryArrayList.get(position).getPicture();
        String name = tryArrayList.get(position).getName();

        holder.textView.setText(name);
        Picasso.get().load(picture).placeholder(R.drawable.custom_bacground).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return tryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView picture;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
            textView = itemView.findViewById(R.id.movie_name);
        }
    }
}
