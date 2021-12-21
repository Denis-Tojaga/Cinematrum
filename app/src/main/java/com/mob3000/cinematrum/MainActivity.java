package com.mob3000.cinematrum;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.ActivityMainBinding;
import com.mob3000.cinematrum.helpers.LocationTracker;
import com.mob3000.cinematrum.ui.account.AccountFragment;
import com.mob3000.cinematrum.ui.home.HomeFragment;
import com.mob3000.cinematrum.ui.wishlist.WishlistFragment;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static int LOCATION_CHANGED_MIN_DISTANCE = 10; // 10 meters
    private static int LOCATION_CHANGED_MIN_TIME = 1000 * 60; // 1 minute
    public Location _location;
    public int distance;
    public boolean usingLocationService;
    private ActivityMainBinding binding;
    private User loggedUser;
    private LocationTracker _locationTracker;
    private int _radius = 5; // initial radius for cinema search


    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_wishlist:
                    selectedFragment = new WishlistFragment();
                    break;
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_account:
                    selectedFragment = new AccountFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(navListener);
        navView.setSelectedItemId(R.id.navigation_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_wishlist, R.id.navigation_home, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragmentLayout);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/


        if (savedInstanceState == null) {

            _locationTracker = new LocationTracker(this, this);
            usingLocationService = _locationTracker.checkPermissions();
        }

        if (savedInstanceState != null) {

            _location = savedInstanceState.getParcelable("location");
            usingLocationService = savedInstanceState.getBoolean("usingLocationService");

        }
        String loggeduser = getIntent().getStringExtra("username");


        /*
         * Denis 10.9.2021
         * */
        //setting the background color of the fragment
        //findViewById(R.id.nav_host_fragment_activity_main).getRootView().setBackgroundColor(getResources().getColor(R.color.background_theme));

        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new HomeFragment1()).commit();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("location", _location);
        outState.putBoolean("usingLocationService", usingLocationService);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        _location = location;
        Log.d("MOVIEDETAILS", location.getLongitude() + " " + location.getLatitude());
        HomeFragment hf = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentLayout);
        if (hf != null) {
            hf.loadMoviesWithLocation(location);
            hf.turnOffLoading();
        }
   }
    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}

