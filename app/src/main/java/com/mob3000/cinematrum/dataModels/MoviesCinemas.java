package com.mob3000.cinematrum.dataModels;

import java.util.ArrayList;
import java.util.Date;

public class MoviesCinemas {
    private int moviesCinemas_id;
    private int movie_id;
    private int hall_id;
    private double price;
    private int seatsAvailable;
    private Date date;
    private ArrayList<Ticket> tickets;

    public int getMoviesCinemas_id() {
        return moviesCinemas_id;
    }

    public void setMoviesCinemas_id(int moviesCinemas_id) {
        this.moviesCinemas_id = moviesCinemas_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public int getHall_id() {
        return hall_id;
    }

    public void setHall_id(int hall_id) {
        this.hall_id = hall_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "MoviesCinemas{" +
                "moviesCinemas_id=" + moviesCinemas_id +
                ", movie_id=" + movie_id +
                ", hall_id=" + hall_id +
                ", price=" + price +
                ", seatsAvailable=" + seatsAvailable +
                ", date=" + date +
                ", tickets=" + tickets +
                '}';
    }
}
