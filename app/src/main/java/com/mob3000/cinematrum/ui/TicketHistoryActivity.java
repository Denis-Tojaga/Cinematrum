package com.mob3000.cinematrum.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.User;

public class TicketHistoryActivity extends AppCompatActivity {

    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        loggedUser =(User)getIntent().getSerializableExtra("user");
    }
}