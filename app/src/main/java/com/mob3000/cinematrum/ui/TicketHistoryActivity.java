package com.mob3000.cinematrum.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.TicketsRecyclerViewAdapter;

public class TicketHistoryActivity extends AppCompatActivity {

    private User loggedUser;
    private RecyclerView recViewTickets;
    private TicketsRecyclerViewAdapter ticketRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();

        InitViews();
        LoadLoggedUser();
        recViewTickets = findViewById(R.id.recViewTickets);
        ticketRecyclerViewAdapter = new TicketsRecyclerViewAdapter(this,"MainActivity");

        //using the user's ticket list to initialize the arrayList of tickets inside an adapter
        ticketRecyclerViewAdapter.set_tickets(loggedUser.getTickets());

        //setting adapter for recyclerView
        recViewTickets.setAdapter(ticketRecyclerViewAdapter);

        //everytime we use custom adapter we need to set a LayoutManager
        recViewTickets.setLayoutManager(new LinearLayoutManager(this));
    }

    private void LoadLoggedUser() {
        try {
            loggedUser = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e) {
            Toast.makeText(this, "Error while loading logged user!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void InitViews() {
        recViewTickets = findViewById(R.id.recViewTickets);
    }
}