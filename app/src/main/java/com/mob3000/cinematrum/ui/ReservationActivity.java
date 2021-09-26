package com.mob3000.cinematrum.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.mob3000.cinematrum.sqlite.DataAcessor;
import com.mob3000.cinematrum.sqlite.DatabaseHelper;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity implements OnItemClickListener {

    public static String INTENT_MOVIE_ID = "movieID";
    public static String INTENT_CINEMA_ID = "cinemaID";
    ImageButton backButton;
    TextView txtMovieName;
    TextView txtCinemaName;
    RecyclerView recyclerView;
    Button btnBuyTicket;
    ReservationRecyclerViewAdapter viewAdapter;
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
        ArrayList<MoviesCinemas> moviesCinemas = DataAcessor.getMoviesCinemasByCinemaId(this, movieId, cinemaId);
        viewAdapter = new ReservationRecyclerViewAdapter(this, moviesCinemas, this);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClick(int index) {
        Toast.makeText(this, String.valueOf(index), Toast.LENGTH_LONG).show();
        viewAdapter.setSelectedPosition(index);
        viewAdapter.notifyDataSetChanged();
    }


    // TODO: FINALIZE
    private void onTicketBuyPressed() {

        // check if a timeslot has been chosen
        if (this.viewAdapter.getSelectedPosition() == -1) {
            Toast.makeText(this, "Please choose a timeslot!", Toast.LENGTH_LONG).show();
            return;
        }

        // ....

        finish();
    }
}