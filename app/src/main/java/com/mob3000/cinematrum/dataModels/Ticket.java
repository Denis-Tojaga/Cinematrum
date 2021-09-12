package com.mob3000.cinematrum.dataModels;

import java.util.Date;

public class Ticket {

    private int ticket_id;
    private int moviesCinemas_id;
    private Date reservedAt;
    private int rowNumber;
    private int seatNumber;

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getMoviesCinemas_id() {
        return moviesCinemas_id;
    }

    public void setMoviesCinemas_id(int moviesCinemas_id) {
        this.moviesCinemas_id = moviesCinemas_id;
    }

    public Date getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Date reservedAt) {
        this.reservedAt = reservedAt;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
