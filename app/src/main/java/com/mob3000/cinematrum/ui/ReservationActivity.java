package com.mob3000.cinematrum.ui;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Cinema;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.MoviesCinemas;
import com.mob3000.cinematrum.dataModels.Ticket;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.helpers.OnItemClickListener;
import com.mob3000.cinematrum.helpers.ReservationRecyclerViewAdapter;
import com.mob3000.cinematrum.helpers.ReservationSpinnerAdapter;
import com.mob3000.cinematrum.helpers.Utils;
import com.mob3000.cinematrum.notification.NotificationReminderBroadcast;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ReservationActivity extends AppCompatActivity implements OnItemClickListener {

    public static String INTENT_MOVIE_ID = "movieID";
    public static String INTENT_CINEMA_ID = "cinemaID";
    public static String SPINNER_ROW_INITIAL_TEXT = "Choose Row";
    public static String SPINNER_SEAT_INITIAL_TEXT = "Choose Seat";
    private static String LOG_TAG = "RESERVATIONACTIVITY";
    MoviesCinemas currentMovieCinema = new MoviesCinemas();
    ArrayList<String> spinnerRowDataSource = new ArrayList<String>(Arrays.asList(SPINNER_ROW_INITIAL_TEXT));
    ArrayList<String> spinnerSeatDataSource = new ArrayList<>(Arrays.asList(SPINNER_SEAT_INITIAL_TEXT));
    int spinnerRowDataSourceLength = spinnerRowDataSource.size() - 1;
    int spinnerSeatDataSourceLength = spinnerSeatDataSource.size() - 1;
    private ImageButton backButton;
    private TextView txtMovieName;
    private TextView txtCinemaName;
    private RecyclerView recyclerView;
    private Button btnBuyTicket;
    private Spinner spinnerRow;
    private Spinner spinnerSeat;
    private ArrayList<MoviesCinemas> moviesCinemas;
    private ReservationRecyclerViewAdapter viewAdapter;
    private ReservationSpinnerAdapter spinnerRowAdapter;
    private ReservationSpinnerAdapter spinnerSeatAdapter;
    private SharedPreferences sp;
    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);

    private int currentSelectedRow = -1;
    private int currentSelectedSeat = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // hide action bar
        getSupportActionBar().hide();

        //creating notification channel
        CreateNotificationChannel();

        backButton = findViewById(R.id.btnGoBack);
        txtMovieName = findViewById(R.id.txtMovieName);
        txtCinemaName = findViewById(R.id.txtCinemaName);
        recyclerView = findViewById(R.id.recyclerView);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        spinnerRow = findViewById(R.id.spinnerRow);
        spinnerSeat = findViewById(R.id.spinnerSeat);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(goBackButtonClick);
                onBackPressed();
            }
        });

        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTicketBuyPressed();
            }
        });

        //get data from intent
        Intent i = getIntent();
        int movieId = i.getIntExtra(INTENT_MOVIE_ID, 0);
        int cinemaId = i.getIntExtra(INTENT_CINEMA_ID, 0);

        // Check passed data
        if (movieId == 0 || cinemaId == 0) {
            Toast.makeText(this, "Error loading movie and cinema. Please try again", Toast.LENGTH_LONG);
        }

        Movie movie = DataAcessor.getMovies(this, DatabaseHelper.COLUMN_MOVIE_movieId, String.valueOf(movieId)).get(0);
        Cinema cinema = DataAcessor.getCinemas(this, DatabaseHelper.COLUMN_CINEMA_cinemaId, String.valueOf(cinemaId)).get(0);

        txtMovieName.setText(movie.getName());
        txtCinemaName.setText(cinema.getName());


        //Setting up recyclerView with moviesCinemas
        moviesCinemas = DataAcessor.getMoviesCinemasByCinemaId(this, movieId, cinemaId);
        viewAdapter = new ReservationRecyclerViewAdapter(this, moviesCinemas, this);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // Setting up spinner and adapter
        spinnerRowAdapter = new ReservationSpinnerAdapter(this, R.layout.spinner_item, spinnerRowDataSource);
        spinnerRow.setAdapter(spinnerRowAdapter);
        spinnerRow.setSelection(spinnerRowDataSourceLength);

        spinnerSeatAdapter = new ReservationSpinnerAdapter(this, R.layout.spinner_item, spinnerSeatDataSource);
        spinnerSeat.setAdapter(spinnerSeatAdapter);
        spinnerSeat.setSelection(spinnerSeatDataSourceLength);


        spinnerRow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSelectedRow = i;

                try {
                    int index = Integer.parseInt(spinnerRowDataSource.get(i));
                    ArrayList<String> newFreeSeats = DataAcessor.getFreeSeatsForRow(getApplicationContext(), currentMovieCinema, index);
                    spinnerSeatDataSource = newFreeSeats;
                    spinnerSeatAdapter.updateData(newFreeSeats);
                    spinnerSeat.setSelection(spinnerRowDataSource.size());

                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage());
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSelectedSeat = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onItemClick(int index) {
        Toast.makeText(this, String.valueOf(index), Toast.LENGTH_LONG).show();
        viewAdapter.setSelectedPosition(index);
        viewAdapter.notifyDataSetChanged(); // Update color of selected/unselected rows.

        // TODO: Load free rows for selected timeslot. Don't show row if there is now free seat in row @tt
        currentMovieCinema = moviesCinemas.get(index);
        spinnerRowDataSource = DataAcessor.getFreeRowsForMovieCinema(this, moviesCinemas.get(index));

        spinnerRowAdapter.updateData(DataAcessor.getFreeRowsForMovieCinema(this, moviesCinemas.get(index)));
        spinnerRow.setSelection(spinnerRowAdapter.getCount() + 1);
    }


    private void onTicketBuyPressed() {

        String alertMessage = "";

        /* VALIDATION */

        // check if a timeslot has been chosen
        if (this.viewAdapter.getSelectedPosition() == -1) {
            alertMessage = "Please choose a timeslot to book a ticket.";
        } else if (currentSelectedRow == -1 || spinnerRowDataSource.get(currentSelectedRow) == SPINNER_ROW_INITIAL_TEXT || currentSelectedSeat == -1 || spinnerSeatDataSource.get(currentSelectedSeat) == SPINNER_SEAT_INITIAL_TEXT) {
            alertMessage = "Please choose Row and seat to book a ticket.";
        }

        if (alertMessage != "") {
            Utils.showOkAlert(this, "Error booking ticket", alertMessage);
            return;
        }

        String userMail = sp.getString("email", "default");
        User currentUser;

        if (userMail != "default") {
            currentUser = DataAcessor.getUser(this, DatabaseHelper.COLUMN_USER_email, userMail).get(0);

            try {

                // .... CREATE TICKET IN DATABASE
                int rowNumber = Integer.parseInt(spinnerRowDataSource.get(currentSelectedRow));
                int seatNumber = Integer.parseInt(spinnerSeatDataSource.get(currentSelectedSeat));

                int movies_cinemas_id = moviesCinemas.get(this.viewAdapter.getSelectedPosition()).getMoviesCinemas_id();
                Ticket ticket = new Ticket(movies_cinemas_id, rowNumber, seatNumber, currentUser.getUser_id());

                boolean ticketInserted = DataAcessor.insertTicket(this, ticket);
                ArrayList<Ticket> allTickets = DataAcessor.getTickets(this, "", "");

                if (!ticketInserted) {
                    Utils.showOkAlert(this, "Error booking ticket.", "An Error accrued when booking your ticket. Please try again.");
                } else {
                    //Extract the date of movie showing for this ticket
                    ArrayList<MoviesCinemas> list = DataAcessor.getMoviesCinemas(this, "moviesCienemas_id", Integer.toString(movies_cinemas_id));
                    MoviesCinemas object = list.get(0);
                    //Converting movie showing date and calling the notifying method
                    ConvertTheDate(object.getDate());

                    finish();
                }
            } catch (Exception ex) {
                Utils.showOkAlert(this, "Error booking ticket.", "An Error accrued when booking your ticket. Please try again.");
            }
        }
    }


    //NOTIFICATION METHODS

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

    //creating a method that will convert the date to time and calculate the time for notifying the user
    private void ConvertTheDate(Date ticketDate) {
        try {
            //first make the formatter we want
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            formatter.setLenient(false);

            //getting the current date and it's millis
            Date currentDate = new Date();
            long currentMillis = currentDate.getTime();

            //formatting the ticketDate and getting it's millis
            String formattedTicketDate = formatter.format(ticketDate);
            Date ticketDateTime = formatter.parse(formattedTicketDate);
            long ticketDateMillis = ticketDateTime.getTime();

            if (currentMillis < ticketDateMillis) {
                //the difference between this data and ticketDate is at what time a notification should pop up minus 60 minutes
                long timeForNotifying = ticketDateMillis - currentMillis - (60 * (60 * 1000));
                //method that will notify the user in this time
                MethodForNotifying(timeForNotifying);
            } else {
                Log.d("ErrorTAG", "Wrong date of showing a movie!");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //creating a method for notifying the user - setting the timer for the given time
    private void MethodForNotifying(long timeForNotifying) {
        Intent intent = new Intent(ReservationActivity.this, NotificationReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ReservationActivity.this, 0, intent, 0);

        //we get the alarm manager that will actually notify us
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = System.currentTimeMillis();

        //not we call the alarm, which type is it, the time in which will we get notified, and what happens when we get notified
        //RTC_WAKEUP - wakes up the device to fire the pending intent at the specified time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time + timeForNotifying, pendingIntent);
    }

}