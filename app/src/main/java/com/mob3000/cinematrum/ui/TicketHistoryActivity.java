package com.mob3000.cinematrum.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mob3000.cinematrum.MainActivity;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.TicketsRecyclerViewAdapter;
import com.mob3000.cinematrum.ui.account.NotificationsFragment;

public class TicketHistoryActivity extends AppCompatActivity {

    private User loggedUser;
    private RecyclerView recViewTickets;
    private TicketsRecyclerViewAdapter ticketRecyclerViewAdapter;
    private ImageButton btnGoBack;


    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();

        InitViews();
        LoadLoggedUser();

        btnGoBack.setOnClickListener(goBackListener);

        recViewTickets = findViewById(R.id.recViewTickets);
        ticketRecyclerViewAdapter = new TicketsRecyclerViewAdapter(this, "MainActivity");

        //using the user's ticket list to initialize the arrayList of tickets inside an adapter
        ticketRecyclerViewAdapter.set_tickets(loggedUser.getTickets());

        //setting adapter for recyclerView
        recViewTickets.setAdapter(ticketRecyclerViewAdapter);

        //everytime we use custom adapter we need to set a LayoutManager
        recViewTickets.setLayoutManager(new LinearLayoutManager(this));
    }


    //method for going back to previous fragment from where this activity is started
    View.OnClickListener goBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.startAnimation(goBackButtonClick);
            onBackPressed();
        }
    };
    @Override
    public void onBackPressed() {
        finish();
    }

    //this method loads the logged user into the user instance
    private void LoadLoggedUser() {
        try {
            loggedUser = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e) {
            Toast.makeText(this, "Error while loading logged user!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //initializing all views from this activity
    private void InitViews() {
        recViewTickets = findViewById(R.id.recViewTickets);
        btnGoBack = findViewById(R.id.btnGoBack);
    }
}