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

public class MovieDetailsActivity extends AppCompatActivity implements LocationListener, AdapterView.OnItemSelectedListener {
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
    private LocationTracker _locationTracker;
    private Location _location;
    private ArrayList<Wishlist> wishlist;
    private List<Cinema> cinemaArrayList;
    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);

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
        }
        String userMail = sp.getString("email", "default");

        if (userMail != "default") {
            ArrayList<User> users = DataAcessor.getUser(this, DatabaseHelper.COLUMN_USER_email, userMail);

            if (users.size() == 1)
                user = users.get(0);
        }
        _locationTracker = new LocationTracker(this, this);
        if (!_locationTracker.checkPermissions()){
            // TODO: Load movie directly because missing permission for location services
            //MovieList = DataAcessor.getMoviesFromLocation(getActivity(), _location, seekBar.getProgress());
            Log.d("HOMEFRAGMENT", "LOADING MOVIES DIRECTLY");
        }
        else {
            // Wait for Location. Load movies in onLocationChanged while passing location - maybe display some loading indicator?
            Log.d("HOMEFRAGMENT", "WAITING FOR LOCATION");
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
        wishlist = new ArrayList<>();
        cinemaArrayList = DataAcessor.getCinemasForMovieFromLocation(this, _location, movieID, distance);
        btnAddToWishlist = findViewById(R.id.btnAddToWishlist);
        btnOpenReservation = findViewById(R.id.btnBuy);
        spinner = findViewById(R.id.spinner);
        if (isInWishlist(movieID)) btnAddToWishlist.setText("REMOVE FROM\nWISHLIST");
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
    public void onLocationChanged(@NonNull Location location) {
        _location = location;
        Log.d("HOMEFRAGMENT", location.getLongitude() + " " + location.getLatitude());

        cinemaArrayList = DataAcessor.getCinemasForMovieFromLocation(this, location, movieID, distance);
        setSpinnerAdapter();

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //cinemaArrayList = DataAcessor.getCinemasForMovieFromLocation(this, _location, movieID, distance);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(getParent().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        btnOpenReservation.setClickable(true);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        btnOpenReservation.setClickable(false);
    }


}