package com.mob3000.cinematrum;

import android.os.Bundle;
import android.widget.ImageView;

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

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    private ActivityHomeScreenBinding binding;
    private User loggedUser;

    private RecyclerView recyclerView;
    private ArrayList<Movie> MovieList;
    private ImageView imageView;
    private LinearLayoutManager layoutManager;



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
    }

    private void setAdapter() {

        MovieAdapter adapter = new MovieAdapter(MovieList);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    void LoadCategoryImages()
    {
        imageView=findViewById(R.id.imageButton);
        Picasso.get().load("https://thoughtcatalog.com/wp-content/uploads/2013/09/istock_000015777770medium2.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=findViewById(R.id.imageButton2);
        Picasso.get().load("https://i.pinimg.com/originals/ef/f8/b9/eff8b9e41133bd9b2b8b733c56b2cbea.jpg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=findViewById(R.id.imageButton3);
        Picasso.get().load("https://miro.medium.com/max/1000/1*T-544XBLkxSr4y_aAo5OfQ.jpeg").placeholder(R.drawable.category_icons).into(imageView);
        imageView=findViewById(R.id.imageButton4);
        Picasso.get().load("https://i.insider.com/5e5036b5a9f40c18895e8d88?width=700").placeholder(R.drawable.category_icons).into(imageView);
    }
}