package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    int movieID;
    TextView textView;
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            movieID = extras.getInt("movieID");
        }

        loadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=N_gD9-Oa0fg"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });

    }

    public void loadData()
    {
        movie = DataAcessor.getMovies(this, DatabaseHelper.COLUMN_MOVIE_movieId, String.valueOf(movieID)).get(0);
        imageView = findViewById(R.id.moviePoster);
        Picasso.get().load(movie.getPicture()).placeholder(R.drawable.custom_bacground).into(imageView);
        //textView = findViewById(R.id.txtCategory);
        //textView.setText(movie.getCategories().toString());
        textView=findViewById(R.id.txtDescription);
        textView.setText(movie.getName());
        textView=findViewById(R.id.txtMovieTitle);
        textView.setText(movie.getName());
        button = findViewById(R.id.btnTrailer);
    }

}