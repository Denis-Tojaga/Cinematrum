package com.mob3000.cinematrum.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Ticket;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.TicketsRecyclerViewAdapter;
import com.mob3000.cinematrum.notification.NotificationReminderBroadcast;

import java.util.ArrayList;

public class TicketHistoryActivity extends AppCompatActivity {

    //instantiating variables
    private User loggedUser;
    private RecyclerView recViewTickets;
    private TicketsRecyclerViewAdapter ticketRecyclerViewAdapter;


    //instantiating views
    private ImageButton btnGoBack;
    private ImageView imgErrorIcon;
    private TextView txtErrorMessage;
    private SearchView svSearchInput;


    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        //removes the UI actionBar at the top of the screen
        getSupportActionBar().hide();

        CreateNotificationChannel();
        InitViews();
        LoadLoggedUser();
        LoadTickets(loggedUser.getTickets());

        //setting onClickListener to go back icon
        btnGoBack.setOnClickListener(goBackListener);

        //setting onQueryTextListener to searchView for filtering tickets on user input
        svSearchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //we simply call the filter method that is implemented in recyclerView
                ticketRecyclerViewAdapter.getFilter().filter(s);
                return false;
            }
        });


    }


    //initializing all views from this activity
    private void InitViews() {
        recViewTickets = findViewById(R.id.recViewTickets);
        btnGoBack = findViewById(R.id.btnGoBack);
        imgErrorIcon = findViewById(R.id.imgErrorIcon);
        txtErrorMessage = findViewById(R.id.txtErrorMessage);
        svSearchInput = findViewById(R.id.svSearchInput);
    }

    //method used to initialize recyclerView and load the user's tickets
    private void LoadTickets(ArrayList<Ticket> tickets) {
        //initializing recyclerView and recyclerViewAdapter
        recViewTickets = findViewById(R.id.recViewTickets);
        ticketRecyclerViewAdapter = new TicketsRecyclerViewAdapter(this);

        //using the user's ticket list to initialize the arrayList of tickets inside an adapter
        if (tickets.size() != 0) {
            ticketRecyclerViewAdapter.set_tickets(tickets);
            //setting adapter for recyclerView
            recViewTickets.setAdapter(ticketRecyclerViewAdapter);
            //everytime we use custom adapter we need to set a LayoutManager
            recViewTickets.setLayoutManager(new LinearLayoutManager(this));
        } else
            ShowNoTicketsMessage();
    }

    //method for hiding recyclerView and showing alert message
    private void ShowNoTicketsMessage() {
        recViewTickets.setVisibility(View.GONE);
        imgErrorIcon.setVisibility(View.VISIBLE);
        txtErrorMessage.setVisibility(View.VISIBLE);
    }

    //method for going back to previous fragment from where this activity is started
    View.OnClickListener goBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.startAnimation(goBackButtonClick);
            onBackPressed();
        }
    };

    //this method loads the logged user into the user instance
    private void LoadLoggedUser() {
        try {
            loggedUser = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e) {
            Toast.makeText(this, "Error while loading logged user!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //creating a notification channel
    private void CreateNotificationChannel() {
        //if the API level is 26 or higher we need to make a notification channel
        Toast.makeText(this, "The notification channel is creating!", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence notificationChannelName = "MovieNotificationChannel";
            String description = "Channel for user notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("MovieNotification", notificationChannelName, importance);
            channel.setDescription(description);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    //logic for triggering the alarm when the notification needs to be sent
    //TODO implement this logic when the user books the ticket and then call it for every movie that he didn't watched
    private void MethodForNotifying(long timeForNotifying) {
        Intent intent = new Intent(TicketHistoryActivity.this, NotificationReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TicketHistoryActivity.this, 0, intent, 0);

        //we get the alarm manager that will actually notify us
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = System.currentTimeMillis();

        //not we call the alarm, which type is it, the time in which will we get notified, and what happens when we get notified
        //RTC_WAKEUP - wakes up the device to fire the pending intent at the specified time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time + timeForNotifying, pendingIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
