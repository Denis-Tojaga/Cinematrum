package com.mob3000.cinematrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.mob3000.cinematrum.dataModels.Cinema;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.dataModels.Wishlist;
import com.mob3000.cinematrum.helpers.LocationTracker;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;
import com.mob3000.cinematrum.ui.ReservationActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
    private SharedPreferences sp;
    private int distance;
    private Cinema selectedCinema;
    private Spinner spinner;
    private Location _location;
    private ArrayList<Wishlist> wishlist;
    private List<Cinema> cinemaArrayList;
    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);
    private boolean usingLocationService;
    private int selectedCinemaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        sp = this.getSharedPreferences("login", MODE_PRIVATE);
        if(extras!=null){
            movieID = extras.getInt("movieID");

            //TODO have to pass the distance from the movie screen as well
            distance = extras.getInt("distance");
            _location = extras.getParcelable("location");
            usingLocationService = extras.getBoolean("usingLocationService");
        }
        String userMail = sp.getString("email", "default");

        if (userMail != "default") {
            ArrayList<User> users = DataAcessor.getUser(this, DatabaseHelper.COLUMN_USER_email, userMail);

            if (users.size() == 1)
                user = users.get(0);
        }
        loadData();
    }

    public void loadData()
    {
        movie = DataAcessor.getMovies(this, DatabaseHelper.TABLENAME_MOVIE + "." +DatabaseHelper.COLUMN_MOVIE_movieId, String.valueOf(movieID)).get(0);
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
        wishlist = new ArrayList<>();
        //cinemaArrayList = DataAcessor.getCinemasForMovieFromLocation(this, _location, movieID, distance);
        btnAddToWishlist = findViewById(R.id.btnAddToWishlist);
        btnOpenReservation = findViewById(R.id.btnBuy);
        spinner = findViewById(R.id.spinner);
        if (isInWishlist(movieID)) btnAddToWishlist.setText("REMOVE FROM\nWISHLIST");
        btnOpenReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                intent.putExtra(ReservationActivity.INTENT_CINEMA_ID, selectedCinemaId);
                intent.putExtra(ReservationActivity.INTENT_MOVIE_ID, movieID);
                startActivity(intent);
            }
        });
        btnAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(goBackButtonClick);
                if (!isInWishlist(movieID)) {
                    DataAcessor.addMovieToWishlist(getApplicationContext(), user.getUser_id(), movieID);
                    btnAddToWishlist.setText("REMOVE FROM\nWISHLIST");
                }
                else {
                    DataAcessor.removeMovieFromWishlist(getApplicationContext(), user.getUser_id(), movieID);
                    btnAddToWishlist.setText("ADD TO\nWISHLIST");

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
        //setSpinnerAdapter();

        if (usingLocationService)
        cinemaArrayList = DataAcessor.getCinemasForMovieFromLocation(this, _location, movieID, distance);
        else cinemaArrayList = DataAcessor.getCinemasForMovie(this, movieID);
        setSpinnerAdapter();

    }
    private void setSpinnerAdapter()
    {
        ArrayAdapter<Cinema> adapter = new ArrayAdapter<Cinema>(this, R.layout.category_spinner_item, cinemaArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    private boolean isInWishlist(int movieID)
    {
        wishlist = user.getWishlist();
        if (wishlist==null) return false;
        for (int i=0; i<wishlist.size(); i++)
        {
            if(wishlist.get(i).getMovie_id()==movieID)
                return true;
        }
        return false;
    }

    private void OpenYoutubeTrailer(String movieTrailerURL) {
        if (movieTrailerURL.contains("youtube.com")) {
            Intent webIntent = new Intent(this, WebActivity.class);
            webIntent.putExtra("url", movieTrailerURL);
            startActivity(webIntent);
        } else
            Log.d("TAG", "Invalid movie trailer URL!");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        selectedCinemaId = cinemaArrayList.get(i).getCinema_id();
        btnOpenReservation.setClickable(true);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedCinemaId = 0;
        btnOpenReservation.setClickable(false);
    }


}