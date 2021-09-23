package com.mob3000.cinematrum.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Cinema;
import com.mob3000.cinematrum.dataModels.Hall;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.MoviesCinemas;
import com.mob3000.cinematrum.dataModels.Ticket;
import com.mob3000.cinematrum.sqlite.DataAcessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TicketsRecyclerViewAdapter extends RecyclerView.Adapter<TicketsRecyclerViewAdapter.ViewHolder> {

    //initializing needed attributes for RecyclerViewAdapter

    //list of all tickets that we are going to show inside our activity
    private ArrayList<Ticket> userTickets;
    //if we are going to use Glide to show images from internet we need to have context in this class, because we are not inside activity
    private Context mContext;

    private String parentActivity;


    //constructor of RecyclerView adapter
    public TicketsRecyclerViewAdapter(Context mContext, String parentActivity) {
        this.mContext = mContext;
        this.parentActivity = parentActivity;
    }

    //method for setting tickets
    public void set_tickets(ArrayList<Ticket> tickets) {
        userTickets = tickets;
        //since we are going to refresh the data inside recycler view we need to add this built in method
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this method will inflate a view with a layout that will present it's values

        //this is why we needed context
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_ticket, parent, false);


        //now that the view is inflated with the correct layout file, we need to pass it to inner class constructor
        //because it will present all properties for that view
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //in this method we manipulate with all the data regarding the one view item
        //setting on click listener, setting data inside something, etc..


        //getting the movies_cinemas object from ticket object
        String mcObjectID = Integer.toString(userTickets.get(position).getMoviesCinemas_id());
        ArrayList<MoviesCinemas> mcObjects = DataAcessor.getMoviesCinemas(mContext, "moviesCienemas_id", mcObjectID);
        MoviesCinemas mcObject = mcObjects.get(0);


        //getting the movie object from movies_cinemas object
        String movieObjectID = Integer.toString(mcObject.getMovie_id());
        ArrayList<Movie> movies = DataAcessor.getMovies(mContext, "movie_id", movieObjectID);
        Movie movieObject = movies.get(0);


        //getting the hall object from movies_cinemas object
        String hallObjectID = Integer.toString(mcObject.getHall_id());
        ArrayList<Hall> halls = DataAcessor.getHalls(mContext, "hall_id", hallObjectID);
        Hall hallObject = halls.get(0);


        //getting the cinema object from hall object
        String cinemaObjectID = Integer.toString(hallObject.getCinema_id());
        ArrayList<Cinema> cinemas = DataAcessor.getCinemas(mContext, "cinema_id", cinemaObjectID);
        Cinema cinemaObject = cinemas.get(0);


        //loading required data from different objects into corresponding views
        Glide.with(mContext).asBitmap().load(movieObject.getPicture()).into(holder.imgMovieWallpaper);
        holder.txtMovieTitle.setText(movieObject.getName());
        holder.txtMoviePrice.setText(Double.toString(mcObject.getPrice()) + "€");
        holder.txtMovieRowNumber.setText(Integer.toString(userTickets.get(position).getRowNumber()));
        holder.txtMovieSeatNumber.setText(Integer.toString(userTickets.get(position).getSeatNumber()));
        holder.txtMovieCinema.setText(cinemaObject.getName() + " " + cinemaObject.getLocation());

        //extracting only date without time from this value
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MMM/yyyy");
        String dateOnly = dateFormat.format(userTickets.get(position).getReservedAt());
        holder.txtMovieReservedAt.setText(dateOnly);
    }


    //returns the number of tickets inside an arrayList
    @Override
    public int getItemCount() {
        return userTickets.size();
    }


    //inner class that will handle the presentation of each view inside recyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        //properties of each ticket
        private ImageView imgMovieWallpaper;
        private TextView txtMovieTitle;
        private TextView txtMoviePrice;
        private TextView txtMovieRowNumber;
        private TextView txtMovieSeatNumber;
        private TextView txtMovieCinema;
        private TextView txtMovieReservedAt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovieWallpaper = itemView.findViewById(R.id.imgMovieWallpaper);
            txtMovieTitle = itemView.findViewById(R.id.txtMovieTitle);
            txtMoviePrice = itemView.findViewById(R.id.txtMoviePrice);
            txtMovieRowNumber = itemView.findViewById(R.id.txtMovieRowNumber);
            txtMovieSeatNumber = itemView.findViewById(R.id.txtMovieSeatNumber);
            txtMovieCinema = itemView.findViewById(R.id.txtMovieCinema);
            txtMovieReservedAt = itemView.findViewById(R.id.txtMovieReservedAt);
        }
    }

}
