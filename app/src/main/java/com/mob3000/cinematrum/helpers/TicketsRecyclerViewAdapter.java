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

import com.mob3000.cinematrum.R;
import com.mob3000.cinematrum.dataModels.Ticket;

import java.util.ArrayList;

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

        //TODO get the moviesccinemas object from sql and set all the data inside of this 


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
