package com.mob3000.cinematrum.helpers;

import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.RecyclerView;

import com.mob3000.cinematrum.MovieDetailsActivity;
import com.mob3000.cinematrum.dataModels.Category;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.MoviesCinemas;
import com.mob3000.cinematrum.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements Filterable
    {

        private ArrayList<Movie> MovieList;
        private MovieClickListener listener;
        private ArrayList<Movie> MovieListAll;


        public MovieAdapter(ArrayList<Movie> movies, Context context, MovieClickListener listener)
        {
            this.MovieList = movies;
            this.listener = listener;
            MovieListAll = new ArrayList<>(MovieList);
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

        @Override
        public Filter getFilter() {
            return filteredList;
        }
        private Filter filteredList = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Movie> filteredList = new ArrayList<>();
                if(constraint == null || constraint.length()==0)
                {
                    filteredList.addAll(MovieListAll);
                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Movie item : MovieListAll)
                    {
                        if(item.getName().toLowerCase().contains(filterPattern))
                        filteredList.add(item);
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                MovieList.clear();
                MovieList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        public Filter filterCategories()
        {
            return categoryFilter;
        }

        public Filter categoryFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Movie> filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Movie item : MovieListAll)
                {
                    for (int i=0; i<item.getCategories().size(); i++)
                    {
                        if(item.getCategories().get(i).getName().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(item);
                        }
                    }
                }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                MovieList.clear();
                if(charSequence!="All")
                    MovieList.addAll((List) filterResults.values);
                else
                    MovieList.addAll(MovieListAll);
                notifyDataSetChanged();
            }
        };

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            private TextView name;
            private ImageView picture;
            private ConstraintLayout parent;

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                name = itemView.findViewById(R.id.movie_name);
                picture = itemView.findViewById(R.id.moviePicture);
                parent = itemView.findViewById(R.id.movieItem);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                listener.onClick(view, getAdapterPosition());
            }
        }
        public interface MovieClickListener{
            void onClick(View v, int position);
        }


}


