package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob3000.cinematrum.dataModels.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


    }
    MovieDetailsActivity()
    {

    }
    MovieDetailsActivity(Movie movie)
    {
        this.movie = movie;
    }

    public void loadData()
    {
        textView = findViewById(R.id.txtMovieTitle);
        textView.setText(movie.getName());
        textView = findViewById(R.id.txtCategory);
        textView.setText(movie.getCategories().toString());
        imageView = findViewById(R.id.moviePoster);
        Picasso.get().load(movie.getPicture()).placeholder(R.drawable.custom_bacground).into(imageView);
    }

}