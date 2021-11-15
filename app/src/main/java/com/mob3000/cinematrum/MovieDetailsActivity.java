package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;
import com.mob3000.cinematrum.ui.ReservationActivity;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    //TODO fix the styling (fonts, same button as the others)
    //TODO add the - addToFavourites button,goBack arrow
    //TODO add the duration to the movie model
    private Movie movie;
    int movieID;
    private TextView txtDescription;
    private TextView txtMovieName;
    private TextView txtRating;
    private TextView txtDuration;
    private TextView txtCategories;
    private ImageView imageView;
    private Button btnAddToWishlist;
    private Button btnTrailer;
    private Button btnOpenReservation;
    private Button btnOpenYoutube;
    private ImageButton btnGoBack;
    private User user;
    private int userID;
    private boolean addorRemove;
    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            movieID = extras.getInt("movieID");
            addorRemove = (extras.getInt("AddOrRemove")==1);
        }
        loadData();
    }

    public void loadData()
    {
        movie = DataAcessor.getMovies(this, DatabaseHelper.COLUMN_MOVIE_movieId, String.valueOf(movieID)).get(0);
        imageView = findViewById(R.id.moviePoster);
        Picasso.get().load(movie.getPicture()).placeholder(R.drawable.custom_bacground).into(imageView);
        btnGoBack = findViewById(R.id.btnGoBackMD);
        txtDescription=findViewById(R.id.txtDescription);
        txtDescription.setText(movie.getDescription());
        txtMovieName=findViewById(R.id.txtMovieTitle);
        txtMovieName.setText(movie.getName());
        txtCategories = findViewById(R.id.txtCategories);
        txtDuration = findViewById(R.id.txtDuration);
        txtRating = findViewById(R.id.txtRating);
        txtCategories.setText(movie.getCategoryNames());
        txtDuration.setText("Duration: " + movie.getDuration());
        txtRating.setText("Rating: " + movie.getRating());
        btnTrailer = findViewById(R.id.btnTrailer);
        user = DataAcessor.getSingleUser(this, userID);
        btnAddToWishlist = findViewById(R.id.btnAddToWishlist);
        btnOpenReservation = findViewById(R.id.btnBuy);
        if (!addorRemove) btnAddToWishlist.setText("REMOVE FROM\nWISHLIST");
        btnOpenReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                intent.putExtra(ReservationActivity.INTENT_CINEMA_ID, 1);
                intent.putExtra(ReservationActivity.INTENT_MOVIE_ID, movieID);
                startActivity(intent);
            }
        });
        btnAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(goBackButtonClick);
                if (addorRemove) {
                    DataAcessor.addMovieToWishlist(getApplicationContext(), userID, movieID);
                }
                else {
                    DataAcessor.removeMovieFromWishlist(getApplicationContext(), userID, movieID);
                }
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(goBackButtonClick);
                onBackPressed();
            }
        });
        btnOpenYoutube = findViewById(R.id.btnTrailer);
        btnOpenYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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