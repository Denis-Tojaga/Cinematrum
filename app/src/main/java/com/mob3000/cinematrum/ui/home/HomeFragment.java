package com.mob3000.cinematrum.ui.home;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mob3000.cinematrum.MovieDetailsActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Category;
import com.mob3000.cinematrum.dataModels.Cinema;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.HomeFragment1FragmentBinding;
import com.mob3000.cinematrum.helpers.CategoryAdapter;
import com.mob3000.cinematrum.helpers.LocationTracker;
import com.mob3000.cinematrum.helpers.MovieAdapter;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements LocationListener {

    private HomeViewModel homeViewModel;
    private MovieAdapter.MovieClickListener movieClickListener;
    private CategoryAdapter.CategoryClickListener categoryClickListener;
    private RecyclerView movieRecyclerView;
    private RecyclerView categoryRecyclerView;
    private ArrayList<Movie> MovieList;
    private ArrayList<Category> CategoryList;
    private LinearLayoutManager movieLayoutManager;
    private LinearLayoutManager categoryLayoutManager;
    private SearchView searchView;
    private MovieAdapter movieAdapter;
    private CategoryAdapter categoryAdapter;
    private TextView seekBarValue;
    private TextView txtWelcome;
    private SeekBar seekBar;
    private User user;
    private View selectedView;
    private SharedPreferences sp;
    private LocationTracker _locationTracker;
    private int selected;
    private ArrayList<Movie> MoviesReplacement;
    private Location _location;
    View root;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    private HomeFragment1FragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = HomeFragment1FragmentBinding.inflate(inflater, container, false);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        movieRecyclerView=root.findViewById(R.id.recyclerViewMovies);
        categoryRecyclerView = root.findViewById(R.id.recyclerViewCategories);
        selectedView = null;
        MovieList = new ArrayList<>();
        CategoryList = new ArrayList<>();
        MoviesReplacement = new ArrayList<>();

        String userMail = sp.getString("email", "default");

        if (userMail != "default") {
            ArrayList<User> users = DataAcessor.getUser(getActivity(), DatabaseHelper.COLUMN_USER_email, userMail);
            if (users.size() == 1)
                user = users.get(0);
        }
        initData();
        setMovieAdapter(MovieList);
        setCategoryAdapter();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initData()
    {
        MovieList = DataAcessor.getMovies(getActivity(),"","");
        CategoryList = DataAcessor.getCategories(getActivity(),"", "");
        txtWelcome = root.findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome, "+user.getName()+" pick a movie");

        //TODO when the slider is moved, take the location and use if for the method that loads all the movies inside that specific radius
        seekBarValue=root.findViewById(R.id.seekBarValue);
        seekBar=root.findViewById(R.id.seekBar);
        searchView= root.findViewById(R.id.searchbar);
        seekBar.setProgress(80);
        seekBarValue.setText(String.valueOf(seekBar.getProgress()+" km"));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
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
                //ArrayList<Movie> newMovies = DataAcessor.getMoviesFromLocation(getActivity(), _location, seekBar.getProgress());
                movieRecyclerView.setAdapter( new MovieAdapter(MovieList, getActivity(), movieClickListener));

            }
        });
        //Location
        _locationTracker = new LocationTracker(getActivity(), this);
        if (!_locationTracker.checkPermissions()){
            // TODO: Load movie directly because missing permission for location services
            //MovieList = DataAcessor.getMoviesFromLocation(getActivity(), _location, seekBar.getProgress());
            Log.d("HOMEFRAGMENT", "LOADING MOVIES DIRECTLY");
        }
        else {
            // Wait for Location. Load movies in onLocationChanged while passing location - maybe display some loading indicator?
            Log.d("HOMEFRAGMENT", "WAITING FOR LOCATION");
        }

    }
    private void setMovieAdapter(ArrayList<Movie> MovieList) {
        setOnClickMovieListener();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                movieAdapter.getFilter().filter(s);
                return false;
            }
        });
        movieAdapter = new MovieAdapter(MovieList, getActivity(), movieClickListener);
        movieLayoutManager = new LinearLayoutManager(getActivity());
        movieLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        movieRecyclerView.setLayoutManager(movieLayoutManager);
        movieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        movieRecyclerView.setAdapter(movieAdapter);

    }
    private void setCategoryAdapter() {
        setOnClickCategoryListener();
        categoryAdapter = new CategoryAdapter(CategoryList, getActivity(), categoryClickListener);
        categoryLayoutManager = new LinearLayoutManager(getActivity());
        categoryLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void setOnClickMovieListener() {
        movieClickListener = new MovieAdapter.MovieClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("movieID", MovieList.get(position).getMovie_id());
                intent.putExtra("distance", seekBar.getProgress());
                startActivity(intent);
            }
        };
    }
    private void setOnClickCategoryListener(){
        categoryClickListener = new CategoryAdapter.CategoryClickListener() {
            @Override
            public void onClick(View v, int position) {
                if(selectedView==v)
                {
                    selectedView.setBackgroundResource(R.drawable.category_background);
                    selectedView=null;
                    movieAdapter.categoryFilter.filter("All");
                }
                else if (selectedView==null) {
                    selectedView = v;
                    selectedView.setBackgroundResource(R.drawable.category_background_light);
                    movieAdapter.categoryFilter.filter(CategoryList.get(position).getName());
                }
                else if(selectedView!=v && selectedView!=null) {
                    v.setBackgroundResource(R.drawable.category_background_light);
                    selectedView.setBackgroundResource(R.drawable.category_background);
                    selectedView=v;
                    movieAdapter.categoryFilter.filter(CategoryList.get(position).getName());
                }
            }
        };
    }


    /*void LoadCategoryImages()
    {
        imageView=root.findViewById(R.id.romanceButton);
        Picasso.get().load("https://thoughtcatalog.com/wp-content/uploads/2013/09/istock_000015777770medium2.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=root.findViewById(R.id.comedyButton);
        Picasso.get().load("https://i.pinimg.com/originals/ef/f8/b9/eff8b9e41133bd9b2b8b733c56b2cbea.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=root.findViewById(R.id.dramaButton);
        Picasso.get().load("https://miro.medium.com/max/1000/1*T-544XBLkxSr4y_aAo5OfQ.jpeg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=root.findViewById(R.id.horrorButton);
        Picasso.get().load("https://i.insider.com/5e5036b5a9f40c18895e8d88?width=700").placeholder(R.drawable.category_icons).into(imageView);
    }*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        _location = location;
        Log.d("MOVIEDETAILS", location.getLongitude() + " " + location.getLatitude());

        // load Cinemas and Movies nearby - Example for @Mirza for Home and Movie Detail Screen.
        ArrayList<Cinema> cinemas1 = DataAcessor.getCinemasForMovieFromLocation(getActivity(), location, 1, 2);
        ArrayList<Cinema> cinemas2 = DataAcessor.getCinemasForMovieFromLocation(getActivity(), location, 1, 50);

        //moviesByLocation = DataAcessor.getMoviesFromLocation(getActivity(), location, seekBar.getProgress());

        //TODO you already have a location and you have the movies with that location, load them into the recycler view
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