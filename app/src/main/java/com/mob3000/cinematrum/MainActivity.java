package com.mob3000.cinematrum;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.ActivityMainBinding;
import com.mob3000.cinematrum.helpers.Utils;
import com.mob3000.cinematrum.sqlite.DataAcessor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private ActivityMainBinding binding;
    private User loggedUser;
    private LocationManager _locationManager;
    private Location _location;

    private int _radius = 5; // initial radius for cinema search
    private static int LOCATION_CHANGED_MIN_DISTANCE = 10; // 10 meters
    private static int LOCATION_CHANGED_MIN_TIME = 1000 * 60; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_wishlist, R.id.navigation_home, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        //save an instance of currently logged user inside this private object User
        LoadLoggedUser();


        //setting the background color of the fragment
        findViewById(R.id.nav_host_fragment_activity_main).getRootView().setBackgroundColor(getResources().getColor(R.color.background_theme));

        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();


        // Location Services

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_CHANGED_MIN_TIME, LOCATION_CHANGED_MIN_DISTANCE, this);

    }

    /* TEST */
    private void searchCinemasInRadius(){
        ArrayList<Movie> moviesInRadius = DataAcessor.getMoviesFromLocation(this, _location, _radius);
        ArrayList<Movie> moviesInRadius2 = DataAcessor.getMoviesFromLocation(this, _location, 50);
    }

    //method for retrieving the logged user
    private void LoadLoggedUser() {
        //TODO method for retrieving the user for given credentials (email and password) has to be implemented
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Utils.showOkAlert(this, "Current Location:", "LAT: " + location.getLatitude() + " LONG: " + location.getLongitude() );
        _location = location;
        searchCinemasInRadius();
    }

}