package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;
import com.mob3000.cinematrum.ui.ReservationActivity;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    int movieID;
    TextView txtDescription;
    TextView txtMovieName;

    ImageView imageView;
    Button button;
    private Button btnOpenReservation;
    private Button btnOpenYoutube;
    Button txtRating;

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

    }

    public void loadData()
    {
        movie = DataAcessor.getMovies(this, DatabaseHelper.COLUMN_MOVIE_movieId, String.valueOf(movieID)).get(0);
        imageView = findViewById(R.id.moviePoster);
        Picasso.get().load(movie.getPicture()).placeholder(R.drawable.custom_bacground).into(imageView);
        //textView = findViewById(R.id.txtCategory);
        //textView.setText(movie.getCategories().toString());
        txtDescription=findViewById(R.id.txtDescription);
        txtDescription.setText(movie.getDescription());
        txtMovieName=findViewById(R.id.txtMovieTitle);
        txtMovieName.setText(movie.getName());
        txtRating = findViewById(R.id.btnDuration);
        txtRating.setText(movie.getRating());
        button = findViewById(R.id.btnTrailer);
        btnOpenReservation = findViewById(R.id.btnBuy);
        btnOpenReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                intent.putExtra(ReservationActivity.INTENT_CINEMA_ID, 1);
                intent.putExtra(ReservationActivity.INTENT_MOVIE_ID, movieID);
                startActivity(intent);
            }
        });
        btnOpenYoutube = findViewById(R.id.btnTrailer);
        btnOpenYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Mirza implement this same logic on another button
                String movieTrailerURL = movie.getMovieTrailerURL();
                OpenYoutubeTrailer(movieTrailerURL);
            }
        });

    }
    private void OpenYoutubeTrailer(String movieTrailerURL) {
        if (movieTrailerURL.contains("youtube.com")) {
            Intent webIntent = new Intent(getApplicationContext(), WebActivity.class);
            webIntent.putExtra("url", movieTrailerURL);
            startActivity(webIntent);
        } else
            Log.d("TAG", "Invalid movie trailer URL!");
    }

}