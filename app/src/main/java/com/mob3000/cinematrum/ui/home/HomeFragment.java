package com.mob3000.cinematrum.ui.home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mob3000.cinematrum.MainActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Cinema;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.WebActivity;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.FragmentHomeBinding;
import com.mob3000.cinematrum.helpers.LocationTracker;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.ui.ReservationActivity;

import java.util.ArrayList;
import java.util.List;




public class HomeFragment extends Fragment implements LocationListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnOpenReservation;
    private Button btnOpenYoutube;

    private LocationTracker _locationTracker;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        btnOpenReservation = root.findViewById(R.id.btnOpenReservation);
        btnOpenReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReservationActivity.class);
                intent.putExtra(ReservationActivity.INTENT_CINEMA_ID, 1);
                intent.putExtra(ReservationActivity.INTENT_MOVIE_ID, 1);
                startActivity(intent);
            }
        });


        //opening movie trailer logic
        btnOpenYoutube = root.findViewById(R.id.btnOpenYoutube);
        btnOpenYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Mirza implement this same logic on another button
                String movieTrailerURL = "Get a movieTrailerURL from the movie object here!!";
                OpenYoutubeTrailer(movieTrailerURL);
            }
        });


        return root;
    }


    //method that opens a new activity for showing movie trailer
    private void OpenYoutubeTrailer(String movieTrailerURL) {
        if (movieTrailerURL.contains("youtube.com")) {
            Intent webIntent = new Intent(getContext(), WebActivity.class);
            webIntent.putExtra("url", movieTrailerURL);
            startActivity(webIntent);
        } else
            Log.d("TAG", "Invalid movie trailer URL!");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TEST
        // Login in user
        User u = DataAcessor.getSingleUser(getActivity().getApplicationContext(), 1);
        DataAcessor.logInUser(getActivity().getApplicationContext(), u);

        //Location
        _locationTracker = new LocationTracker(getActivity(), this);
        if (!_locationTracker.checkPermissions()){
            // TODO: Load movie directly because missing permission for location services

            Log.d("HOMEFRAGMENT", "LOADING MOVIES DIRECTLY");
        }
        else {
            // Wait for Location. Load movies in onLocationChanged while passing location - maybe display some loading indicator?

            Log.d("HOMEFRAGMENT", "WAITING FOR LOCATION");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        _locationTracker.stopTracking();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("HOMEFRAGMENT", location.getLongitude() + " " + location.getLatitude());

        // load Cinemas and Movies nearby - Example for @Mirza for Home and Movie Detail Screen.
        ArrayList<Cinema> cinemas1 = DataAcessor.getCinemasForMovieFromLocation(getActivity(), location, 1, 2);
        ArrayList<Cinema> cinemas2 = DataAcessor.getCinemasForMovieFromLocation(getActivity(), location, 1, 50);

        ArrayList<Movie> movies1 = DataAcessor.getMoviesFromLocation(getActivity(), location, 1);
        ArrayList<Movie> movies2 = DataAcessor.getMoviesFromLocation(getActivity(), location, 50);

    }


}