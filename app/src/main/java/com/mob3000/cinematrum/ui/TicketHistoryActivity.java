package com.mob3000.cinematrum.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.User;

public class TicketHistoryActivity extends AppCompatActivity {

    private User loggedUser;
    private RecyclerView recViewTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        InitViews();
        LoadLoggedUser();
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