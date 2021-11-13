package com.mob3000.cinematrum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.ActivityHomeScreenBinding;
import com.mob3000.cinematrum.helpers.MovieAdapter;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeFragment extends AppCompatActivity {

    private ActivityHomeScreenBinding binding;
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
    private boolean comedyClick=false;
    private boolean horrorClick=false;
    private boolean dramaClick=false;
    private boolean romanceClick=false;
    private TextView txtRomance;
    private TextView txtComedy;
    private TextView txtDrama;
    private TextView txtHorror;
    private TextView seekBarValue;
    private SeekBar seekBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_home_screen);

        recyclerView=findViewById(R.id.recycleViewHomeScreen);
        MovieList = new ArrayList<>();

        //to hide the action bar
        getSupportActionBar().hide();
        //to load the category images from urls
        LoadCategoryImages();

        initData();
        setAdapter();

        /*BottomNavigationView navView = findViewById(R.id.nav_view_hs);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_view_hs);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewHs, navController);*/


    }

    private void initData()
    {
        MovieList = DataAcessor.getMovies(this.getApplicationContext(),"","");
        comedy = findViewById(R.id.comedyButton);
        drama = findViewById(R.id.dramaButton);
        horror = findViewById(R.id.horrorButton);
        romance = findViewById(R.id.romanceButton);
        txtRomance=findViewById(R.id.txtRomance);
        txtComedy=findViewById(R.id.txtComedy);
        txtHorror=findViewById(R.id.txtHorror);
        txtDrama=findViewById(R.id.txtDrama);
        seekBarValue=findViewById(R.id.seekBarValue);
        seekBar=findViewById(R.id.seekBar);
        seekBar.setProgress(5);
        seekBarValue.setText(String.valueOf(seekBar.getProgress()+" km"));
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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(progress==100)
                    seekBarValue.setText(String.valueOf(progress + "+ km"));
                else
                    seekBarValue.setText(String.valueOf(progress + " km"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        drama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDoubleTap(txtDrama)) {
                    TextColourChange();
                    adapter.categoryFilter.filter("All");
                }
                else {
                    TextColourChange();
                    txtDrama.setTextColor(Color.YELLOW);
                    adapter.categoryFilter.filter("Drama");
                }
            }
        });
        horror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDoubleTap(txtHorror)) {
                    TextColourChange();
                    adapter.categoryFilter.filter("All");
                }
                else {
                    TextColourChange();
                    txtHorror.setTextColor(Color.YELLOW);
                    adapter.categoryFilter.filter("Horror");
                }
            }
        });
        romance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDoubleTap(txtRomance)) {
                    TextColourChange();
                    adapter.categoryFilter.filter("Romance");
                }
                else {
                    TextColourChange();
                    txtRomance.setTextColor(Color.YELLOW);
                    adapter.categoryFilter.filter("All");
                }

            }
        });

    }

    private void setAdapter() {
        setOnClickListener();
        searchView= findViewById(R.id.searchbar);
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
        adapter = new MovieAdapter(MovieList, this, listener);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void setOnClickListener() {
        listener = new MovieAdapter.MovieClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra("movieID", MovieList.get(position).getMovie_id());
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
        imageView=findViewById(R.id.romanceButton);
        Picasso.get().load("https://thoughtcatalog.com/wp-content/uploads/2013/09/istock_000015777770medium2.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=findViewById(R.id.comedyButton);
        Picasso.get().load("https://i.pinimg.com/originals/ef/f8/b9/eff8b9e41133bd9b2b8b733c56b2cbea.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=findViewById(R.id.dramaButton);
        Picasso.get().load("https://miro.medium.com/max/1000/1*T-544XBLkxSr4y_aAo5OfQ.jpeg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=findViewById(R.id.horrorButton);
        Picasso.get().load("https://i.insider.com/5e5036b5a9f40c18895e8d88?width=700").placeholder(R.drawable.category_icons).into(imageView);
    }
}