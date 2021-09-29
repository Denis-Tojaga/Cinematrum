package com.mob3000.cinematrum.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.mob3000.cinematrum.helpers.OnItemClickListener;
import com.mob3000.cinematrum.helpers.ReservationRecyclerViewAdapter;
import com.mob3000.cinematrum.helpers.ReservationSpinnerAdapter;
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ReservationActivity extends AppCompatActivity implements OnItemClickListener {

    public static String INTENT_MOVIE_ID = "movieID";
    public static String INTENT_CINEMA_ID = "cinemaID";
    private static String LOG_TAG = "RESERVATIONACTIVITY";

    public  static String SPINNER_ROW_INITIAL_TEXT = "Choose Row";
    public static String SPINNER_SEAT_INITIAL_TEXT = "Choose Seat";

    ImageButton backButton;
    TextView txtMovieName;
    TextView txtCinemaName;
    RecyclerView recyclerView;
    Button btnBuyTicket;
    Spinner spinnerRow;
    Spinner spinnerSeat;
    ArrayList<MoviesCinemas> moviesCinemas;
    ReservationRecyclerViewAdapter viewAdapter;
    ReservationSpinnerAdapter spinnerRowAdapter;
    ReservationSpinnerAdapter spinnerSeatAdapter;

    MoviesCinemas currentMovieCinema = new MoviesCinemas();
    ArrayList<String> spinnerRowDataSource = new ArrayList<String>(Arrays.asList(SPINNER_ROW_INITIAL_TEXT));
    ArrayList<String> spinnerSeatDataSource = new ArrayList<>(Arrays.asList(SPINNER_SEAT_INITIAL_TEXT));
    int spinnerRowDataSourceLength = spinnerRowDataSource.size() - 1;
    int spinnerSeatDataSourceLength = spinnerSeatDataSource.size() - 1;
    private AlphaAnimation goBackButtonClick = new AlphaAnimation(0.3F, 0.1F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // hide action bar
        getSupportActionBar().hide();

        backButton = findViewById(R.id.btnGoBack);
        txtMovieName = findViewById(R.id.txtMovieName);
        txtCinemaName = findViewById(R.id.txtCinemaName);
        recyclerView = findViewById(R.id.recyclerView);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        spinnerRow = findViewById(R.id.spinnerRow);
        spinnerSeat = findViewById(R.id.spinnerSeat);

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
        Cinema cinema= DataAcessor.getCinemas(this, DatabaseHelper.COLUMN_CINEMA_cinemaId, String.valueOf(cinemaId)).get(0);

        txtMovieName.setText(movie.getName());
        txtCinemaName.setText(cinema.getName());


        //Setting up recyclerView
        moviesCinemas = DataAcessor.getMoviesCinemasByCinemaId(this, movieId, cinemaId);
        viewAdapter = new ReservationRecyclerViewAdapter(this, moviesCinemas, this);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // Setting up spinner and adapter
     /*   spinnerRowAdapter = new ArrayAdapter(this,R.layout.spinner_item, spinnerRowDataSource){
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }

        };*/
        spinnerRowAdapter = new ReservationSpinnerAdapter(this, R.layout.spinner_item, spinnerRowDataSource);
        spinnerRow.setAdapter(spinnerRowAdapter);
        spinnerRow.setSelection(spinnerRowDataSourceLength);
        //android.R.layout.simple_spinner_dropdown_item
        /*spinnerSeatAdapter = new ArrayAdapter(this,R.layout.spinner_item, spinnerSeatDataSource){
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };*/
        spinnerSeatAdapter = new ReservationSpinnerAdapter(this, R.layout.spinner_item, spinnerSeatDataSource);
        spinnerSeat.setAdapter(spinnerSeatAdapter);
        spinnerSeat.setSelection(spinnerSeatDataSourceLength);


        spinnerRow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    int index = Integer.parseInt(spinnerRowDataSource.get(i));
                    ArrayList<String> newFreeSeats = DataAcessor.getFreeSeatsForRow(getApplicationContext(), currentMovieCinema, index);
                    spinnerSeatDataSource = newFreeSeats;
                    spinnerSeatAdapter.updateData(newFreeSeats);
                    spinnerSeat.setSelection(spinnerRowDataSource.size());

                }
                catch(Exception ex){
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

        // TODO: Load free rows for selected timeslot.
        currentMovieCinema = moviesCinemas.get(index);
        spinnerRowDataSource = DataAcessor.getFreeRowsForMovieCinema(this, moviesCinemas.get(index));

        spinnerRowAdapter.updateData(DataAcessor.getFreeRowsForMovieCinema(this, moviesCinemas.get(index)));
        spinnerRow.setSelection(spinnerRowAdapter.getCount() + 1);
    }


    // TODO: FINALIZE
    private void onTicketBuyPressed() {

        // check if a timeslot has been chosen
        if (this.viewAdapter.getSelectedPosition() == -1) {
            Toast.makeText(this, "Please choose a timeslot!", Toast.LENGTH_LONG).show();
            return;
        }

        // .... CREATE TICKET IN DATABASE

        finish();
    }

    // TODO: FINISH!
    private void updateSpinner(){
        // Load new Data

        // Update Data Length

        // NOTIFY ADAPTER CHANGED
    }
}