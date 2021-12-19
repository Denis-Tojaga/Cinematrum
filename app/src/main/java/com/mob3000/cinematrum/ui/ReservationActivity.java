package com.mob3000.cinematrum.ui;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
    private MoviesCinemas currentMovieCinema = new MoviesCinemas();
    private ArrayList<String> spinnerRowDataSource = new ArrayList<>();
    private ArrayList<String> spinnerSeatDataSource = new ArrayList<>();
    private int spinnerRowDataSourceLength = spinnerRowDataSource.size() - 1;
    private int spinnerSeatDataSourceLength = spinnerSeatDataSource.size() - 1;
    private ImageButton backButton;
    private TextView txtMovieName;
    private TextView txtCinemaName;
    private TextView txtChooseRow;
    private TextView txtChooseSeat;
    private RecyclerView recyclerView;
    private Button btnBuyTicket;
    private ArrayList<MoviesCinemas> moviesCinemas;
    private ArrayAdapter<String> spinnerRowAdapter;
    private ArrayAdapter<String> spinnerSeatAdapter;
    private ReservationRecyclerViewAdapter viewAdapter;
    private ImageView imgChooseRow;
    private ImageView imgChooseSeat;
    private SharedPreferences sp;
    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);

    private int currentSelectedRow = -1;
    private int currentSelectedSeat = -1;

    int ID_AUTOINCREMENT = 1;

    View.OnClickListener chooseRowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (viewAdapter.getSelectedPosition() == -1) {
                Utils.showOkAlert(ReservationActivity.this, "Choose timeslot", "Please choose a timeslot first.");
            } else {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ReservationActivity.this);
                builderSingle.setTitle("Select row");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(spinnerRowAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // set currentRow
                        currentSelectedRow = which;
                        txtChooseRow.setText(spinnerRowDataSource.get(which));

                        // Update seats
                        int index = Integer.parseInt(spinnerRowDataSource.get(which));
                        ArrayList<String> newFreeSeats = DataAcessor.getFreeSeatsForRow(getApplicationContext(), currentMovieCinema, index);
                        spinnerSeatDataSource = newFreeSeats;
                        spinnerSeatAdapter.clear();
                        spinnerSeatAdapter.addAll(spinnerSeatDataSource);

                        dialog.dismiss();
                    }
                });
                builderSingle.show();
            }

        }
    };

    View.OnClickListener chooseSeatClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (currentSelectedRow == -1) {
                Utils.showOkAlert(ReservationActivity.this, "Choose row", "Please choose a row before selecting a seat.");
            } else {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ReservationActivity.this);
                builderSingle.setTitle("Select seat");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(spinnerSeatAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // set currentRow
                        currentSelectedSeat = which;
                        txtChooseSeat.setText(spinnerSeatDataSource.get(which));
                        dialog.dismiss();
                    }
                });
                builderSingle.show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // hide action bar
        getSupportActionBar().hide();

        //creating notification channel
        CreateNotificationChannel();

        // initalise views
        backButton = findViewById(R.id.btnGoBack);
        txtMovieName = findViewById(R.id.txtMovieName);
        txtCinemaName = findViewById(R.id.txtCinemaName);
        recyclerView = findViewById(R.id.recyclerView);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        txtChooseRow = findViewById(R.id.txtChooseRow);
        txtChooseSeat = findViewById(R.id.txtChooseSeat);
        imgChooseRow = findViewById(R.id.imgChooseRow);
        imgChooseSeat = findViewById(R.id.imgChooseSeat);

        txtChooseRow.setText(SPINNER_ROW_INITIAL_TEXT);
        txtChooseSeat.setText(SPINNER_SEAT_INITIAL_TEXT);

        sp = getSharedPreferences("login", MODE_PRIVATE);


        spinnerRowAdapter = new ArrayAdapter<String>(ReservationActivity.this, android.R.layout.select_dialog_singlechoice);
        spinnerSeatAdapter = new ArrayAdapter<String>(ReservationActivity.this, android.R.layout.select_dialog_singlechoice);


        // set up listener
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

        txtChooseRow.setOnClickListener(chooseRowClickListener);
        txtChooseSeat.setOnClickListener(chooseSeatClickListener);
        imgChooseRow.setOnClickListener(chooseRowClickListener);
        imgChooseSeat.setOnClickListener(chooseSeatClickListener);


        //get data from intent
        Intent i = getIntent();
        int movieId = i.getIntExtra(INTENT_MOVIE_ID, 0);
        int cinemaId = i.getIntExtra(INTENT_CINEMA_ID, 0);

        // Check passed data
        if (movieId == 0 || cinemaId == 0) {
            Toast.makeText(this, "Error loading movie and cinema. Please try again", Toast.LENGTH_LONG).show();
        }

        Movie movie = new Movie();
        Cinema cinema = new Cinema();

        try {
            movie = DataAcessor.getMovies(this, DatabaseHelper.TABLENAME_MOVIE + "."+ DatabaseHelper.COLUMN_MOVIE_movieId, String.valueOf(movieId)).get(0);
            cinema = DataAcessor.getCinemas(this, DatabaseHelper.COLUMN_CINEMA_cinemaId, String.valueOf(cinemaId)).get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        txtMovieName.setText(movie.getName());
        txtCinemaName.setText(cinema.getName());


        //Setting up recyclerView with moviesCinemas
        moviesCinemas = DataAcessor.getMoviesCinemasByCinemaId(this, movieId, cinemaId);
        viewAdapter = new ReservationRecyclerViewAdapter(this, moviesCinemas, this);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


    }

    @Override
    public void onItemClick(int index) {
        viewAdapter.setSelectedPosition(index);
        viewAdapter.notifyDataSetChanged(); // Update color of selected/unselected rows.

        currentMovieCinema = moviesCinemas.get(index);
        spinnerRowDataSource = DataAcessor.getFreeRowsForMovieCinema(this, moviesCinemas.get(index));
        spinnerRowAdapter.clear();
        spinnerRowAdapter.addAll(spinnerRowDataSource);

        txtChooseSeat.setText(SPINNER_SEAT_INITIAL_TEXT);
        currentSelectedSeat = -1;
        txtChooseRow.setText(SPINNER_ROW_INITIAL_TEXT);
        currentSelectedRow = -1;
    }

    private void onTicketBuyPressed() {

        String alertMessage = "";

        /* VALIDATION */

        // check if a timeslot has been chosen
        if (this.viewAdapter.getSelectedPosition() == -1) {
            alertMessage = "Please choose a timeslot to book a ticket.";
        } else if (currentSelectedRow == -1 || currentSelectedSeat == -1) {
            alertMessage = "Please choose row and seat to book a ticket.";
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MovieNotification", "MovieNotificationChannel", NotificationManager.IMPORTANCE_DEFAULT);
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
        //tested with 10 seconds and it works
        //long tenSeconds = 1000 * 10;

        //not we call the alarm, which type is it, the time in which will we get notified, and what happens when we get notified
        //RTC_WAKEUP - wakes up the device to fire the pending intent at the specified time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time + timeForNotifying, pendingIntent);
    }

}

