package com.mob3000.cinematrum.dataModels;

import java.util.List;

public class Hall {

    private int hall_id;
    private int cinema_id;
    private int rows;
    private int seatRows;
    private List<MoviesCinemas> moviesCinemas;

    public int getHall_id() {
        return hall_id;
    }

    public void setHall_id(int hall_id) {
        this.hall_id = hall_id;
    }

    public int getCinema_id() {
        return cinema_id;
    }

    public void setCinema_id(int cinema_id) {
        this.cinema_id = cinema_id;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatRows() {
        return seatRows;
    }

    public void setSeatRows(int seatRows) {
        this.seatRows = seatRows;
    }

    public List<MoviesCinemas> getMoviesCinemas() {
        return moviesCinemas;
    }

    public void setMoviesCinemas(List<MoviesCinemas> moviesCinemas) {
        this.moviesCinemas = moviesCinemas;
    }
}
