package com.mob3000.cinematrum;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        LoadLoggedUser();


        //setting the background color of the fragment
        findViewById(R.id.nav_host_fragment_activity_main).getRootView().setBackgroundColor(getResources().getColor(R.color.background_theme));

        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();

    }


    //method for retrieving the logged user
    private void LoadLoggedUser() {
        //TODO this has to be fixed, we should retrieve the logged user from getLoggedUserMethod which is not implemented when inserting user
        /*User loggedUser = (User)getIntent().getSerializableExtra("User");
        Toast.makeText(MainActivity.this, loggedUser.getName(), Toast.LENGTH_SHORT).show();*/
    }

}