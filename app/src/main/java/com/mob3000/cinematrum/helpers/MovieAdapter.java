package com.mob3000.cinematrum.helpers;

import android.view.ViewGroup;
import android.widget.Filterable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mob3000.cinematrum.dataModels.Category;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.MoviesCinemas;
import com.mob3000.cinematrum.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>
    {

        private ArrayList<Movie> MovieList;


        public MovieAdapter(ArrayList<Movie> movies)
        {
            this.MovieList = movies;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
            return new MovieAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            String picture = MovieList.get(position).getPicture();
            String name = MovieList.get(position).getName();

            holder.name.setText(name);
            Picasso.get().load(picture).placeholder(R.drawable.custom_bacground).into(holder.picture);

        }


        @Override
        public int getItemCount() {
            return MovieList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView name;
            private ImageView picture;

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                name = itemView.findViewById(R.id.movie_name);
                picture = itemView.findViewById(R.id.picture);
            }
        }



}


