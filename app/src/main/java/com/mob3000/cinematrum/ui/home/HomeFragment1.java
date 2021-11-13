package com.mob3000.cinematrum.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mob3000.cinematrum.MovieDetailsActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Cinema;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.ActivityHomeScreenBinding;
import com.mob3000.cinematrum.databinding.FragmentDashboardBinding;
import com.mob3000.cinematrum.databinding.HomeFragment1FragmentBinding;
import com.mob3000.cinematrum.helpers.LocationTracker;
import com.mob3000.cinematrum.helpers.MovieAdapter;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.ui.dashboard.DashboardViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment1 extends Fragment implements LocationListener {

    private HomeFragment1ViewModel homeViewModel;
    private User loggedUser;
    private MovieAdapter.MovieClickListener listener;
    private RecyclerView recyclerView;
    private ArrayList<Movie> MovieList;
    private ImageView imageView;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;
    private MovieAdapter adapter;
    private ImageView comedy;
    private ImageView horror;
    private ImageView drama;
    private ImageView romance;
    private TextView txtRomance;
    private TextView txtComedy;
    private TextView txtDrama;
    private TextView txtHorror;
    private TextView seekBarValue;
    private TextView txtWelcome;
    private SeekBar seekBar;
    private User user;
    private LocationTracker _locationTracker;
    private ArrayList<Movie> moviesByLocation;
    View root;

    public static HomeFragment1 newInstance() {
        return new HomeFragment1();
    }
    private HomeFragment1FragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        homeViewModel =
                new ViewModelProvider(this).get(HomeFragment1ViewModel.class);

        binding = HomeFragment1FragmentBinding.inflate(inflater, container, false);
        root = inflater.inflate(R.layout.activity_home_screen, container, false);
        recyclerView=root.findViewById(R.id.recycleViewHomeScreen);

        user = DataAcessor.getSingleUser(getActivity().getApplicationContext(), 1);
        DataAcessor.logInUser(getActivity().getApplicationContext(), user);

        //Location
        _locationTracker = new LocationTracker(getActivity(), this);
        if (!_locationTracker.checkPermissions()){
            // TODO: Load movie directly because missing permission for location services

            Log.d("HOMEFRAGMENT", "LOADING MOVIES DIRECTLY");
            moviesByLocation = DataAcessor.getMovies(getActivity(),"","");

        }
        else {
            // Wait for Location. Load movies in onLocationChanged while passing location - maybe display some loading indicator?

            Log.d("HOMEFRAGMENT", "WAITING FOR LOCATION");
            onLocationChanged(_locationTracker.getLocation());
        }


        MovieList = new ArrayList<>();
        moviesByLocation = new ArrayList<>();
        LoadCategoryImages();
        initData();
        setAdapter();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initData()
    {
        //TODO make a recycler view for categories and set up the onClick method inside it
        //TODO change the database schema - add the string catImgURL attribute


        //TODO be consistent with the fonts
        moviesByLocation = DataAcessor.getMovies(getActivity(),"","");
        comedy = root.findViewById(R.id.comedyButton);
        drama = root.findViewById(R.id.dramaButton);
        horror = root.findViewById(R.id.horrorButton);
        romance = root.findViewById(R.id.romanceButton);
        txtRomance=root.findViewById(R.id.txtRomance);
        txtComedy=root.findViewById(R.id.txtComedy);
        txtHorror=root.findViewById(R.id.txtHorror);
        txtDrama=root.findViewById(R.id.txtDrama);
        txtWelcome = root.findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome, "+user.getName()+" pick a movie");

        //TODO when the slider is moved, take the location and use if for the method that loads all the movies inside that specific radius
        seekBarValue=root.findViewById(R.id.seekBarValue);
        seekBar=root.findViewById(R.id.seekBar);
        searchView= root.findViewById(R.id.searchbar);
        seekBar.setProgress(5);
        seekBarValue.setText(String.valueOf(seekBar.getProgress()+" km"));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                onLocationChanged(_locationTracker.getLocation());
                if(progress==100) {
                    seekBarValue.setText(String.valueOf(progress + "+ km"));
                }
                else {
                    seekBarValue.setText(String.valueOf(progress + " km"));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        comedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDoubleTap(txtComedy))
                {
                    TextColourChange();
                    adapter.categoryFilter.filter("All");
                }
                else {
                    TextColourChange();
                    txtComedy.setTextColor(Color.YELLOW);
                    adapter.categoryFilter.filter("Comedy");
                }

            }
        });
        drama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDoubleTap(txtDrama)) {
                    TextColourChange();
                    adapter.filterCategories().filter("All");
                }
                else {
                    TextColourChange();
                    txtDrama.setTextColor(Color.YELLOW);
                    adapter.filterCategories().filter("Drama");
                }
            }
        });
        horror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDoubleTap(txtHorror)) {
                    TextColourChange();
                    adapter.filterCategories().filter("All");
                }
                else {
                    TextColourChange();
                    txtHorror.setTextColor(Color.YELLOW);
                    adapter.filterCategories().filter("Horror");
                }
            }
        });
        romance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDoubleTap(txtRomance)) {
                    TextColourChange();
                    adapter.filterCategories().filter("All");
                }
                else {
                    TextColourChange();
                    txtRomance.setTextColor(Color.YELLOW);
                    adapter.filterCategories().filter("Romance");
                }

            }
        });


    }
    private void setAdapter() {
        setOnClickListener();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        adapter = new MovieAdapter(moviesByLocation, getActivity(), listener);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void setOnClickListener() {
        listener = new MovieAdapter.MovieClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("movieID", MovieList.get(1).getMovie_id());
                intent.putExtra("distance", seekBar.getProgress());
                startActivity(intent);
            }
        };

    }

    boolean checkDoubleTap(TextView textView)
    {
        if (textView.getCurrentTextColor()==Color.YELLOW)
            return true;
        return false;
    }

    void TextColourChange()
    {
        txtComedy.setTextColor(Color.WHITE);
        txtHorror.setTextColor(Color.WHITE);
        txtRomance.setTextColor(Color.WHITE);
        txtDrama.setTextColor(Color.WHITE);
    }
    void LoadCategoryImages()
    {
        imageView=root.findViewById(R.id.romanceButton);
        Picasso.get().load("https://thoughtcatalog.com/wp-content/uploads/2013/09/istock_000015777770medium2.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=root.findViewById(R.id.comedyButton);
        Picasso.get().load("https://i.pinimg.com/originals/ef/f8/b9/eff8b9e41133bd9b2b8b733c56b2cbea.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=root.findViewById(R.id.dramaButton);
        Picasso.get().load("https://miro.medium.com/max/1000/1*T-544XBLkxSr4y_aAo5OfQ.jpeg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=root.findViewById(R.id.horrorButton);
        Picasso.get().load("https://i.insider.com/5e5036b5a9f40c18895e8d88?width=700").placeholder(R.drawable.category_icons).into(imageView);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeFragment1ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("HOMEFRAGMENT", location.getLongitude() + " " + location.getLatitude());

        // load Cinemas and Movies nearby - Example for @Mirza for Home and Movie Detail Screen.
        ArrayList<Cinema> cinemas1 = DataAcessor.getCinemasForMovieFromLocation(getActivity(), location, 1, 2);
        ArrayList<Cinema> cinemas2 = DataAcessor.getCinemasForMovieFromLocation(getActivity(), location, 1, 50);

        moviesByLocation = DataAcessor.getMoviesFromLocation(getActivity(), location, seekBar.getProgress());

        //TODO you already have a location and you have the movies with that location, load them into the recycler view


    }
}