package com.mob3000.cinematrum.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TicketsRecyclerViewAdapter extends RecyclerView.Adapter<TicketsRecyclerViewAdapter.ViewHolder> implements Filterable {

    //initializing needed attributes for RecyclerViewAdapter


    //list of all tickets that we are going to show inside our activity
    private ArrayList<Ticket> userTickets;
    private ArrayList<Ticket> exampleUserTickets;
    //if we are going to use Glide to show images from internet we need to have context in this class, because we are not inside activity
    private Context mContext;



    //constructor of RecyclerView adapter
    public TicketsRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    //method for setting tickets
    public void set_tickets(ArrayList<Ticket> tickets) {
        userTickets = tickets;
        exampleUserTickets = new ArrayList<Ticket>(userTickets);
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
        holder.txtMovieCinema.setText(cinemaObject.getName() + " " + cinemaObject.getName());

        //extracting only date without time from this value
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        String ticketDate = dateFormat.format(userTickets.get(position).getReservedAt());
        holder.txtMovieReservedAt.setText(ticketDate);
        NotifyTheUser(ticketDate);
    }


    //TODO implement this method when the user buys a ticket
    private void NotifyTheUser(String ticketDate) {
        try{
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

            if(currentMillis < ticketDateMillis)
            {
                Log.d("TAG", "NotifyTheUser: Datum karte je stariji");
                //TODO set the notification for this millis

                //the difference between this data and ticketDate is at what time a notification should pop up minus 60 minutes
                //because a notification should be an hour before the movie starts
                long timeForNotifying = ticketDateMillis - currentMillis - (60 * (60 * 1000));


                //TODO call the method for notifying

            }else{
                //TODO dont do anything
            }

        }catch (ParseException e)
        {
            e.printStackTrace();
        }
    }


    //returns the number of tickets inside an arrayList
    @Override
    public int getItemCount() {
        return userTickets.size();
    }



    //method which performs filtering
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            //here we perform out filtering logic
            //this method is executed in the background thread which means if we have something complicated it wont affect out performance
            ArrayList<Ticket> filteredList = new ArrayList<Ticket>();

            if (charSequence == null || charSequence.length() == 0)
                filteredList.addAll(exampleUserTickets);
            else {
                String filter = charSequence.toString().toLowerCase();
                for (Ticket item : exampleUserTickets) {
                    //first getting the movies_cinemas object
                    String mcObjectID = Integer.toString(item.getMoviesCinemas_id());
                    ArrayList<MoviesCinemas> mcObjects = DataAcessor.getMoviesCinemas(mContext, "moviesCienemas_id", mcObjectID);
                    MoviesCinemas mcObject = mcObjects.get(0);

                    //getting the movie object from movies_cinemas object
                    String movieObjectID = Integer.toString(mcObject.getMovie_id());
                    ArrayList<Movie> movies = DataAcessor.getMovies(mContext, "movie_id", movieObjectID);
                    Movie movieObject = movies.get(0);

                    //if movie_name contains any letter from filter text we add that ticket to the new list
                    if (movieObject.getName().toLowerCase().contains(filter))
                        filteredList.add(item);
                }
            }


            //we return our filtered list to out method publishResults which takes it as an argument
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //here the results will be automatically published with UI thread
            //we clear our original list of tickets and fill it with result data
            userTickets.clear();
            userTickets.addAll((ArrayList<Ticket>) filterResults.values);
            //then we notify that the dataset has changed
            notifyDataSetChanged();
        }
    };


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
