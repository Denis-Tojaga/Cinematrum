package com.mob3000.cinematrum.dataModels;

import java.io.Serializable;
import java.util.Date;

public class Ticket implements Serializable {

    private int ticket_id;
    private int moviesCinemas_id;
    private Date reservedAt;
    private int rowNumber;
    private int seatNumber;
    private int user_id;


    public Ticket() {
    }

    public Ticket(int moviesCinemas_id, int rowNumber, int seatNumber, int user_id) {
        this.moviesCinemas_id = moviesCinemas_id;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket_id=" + ticket_id +
                ", moviesCinemas_id=" + moviesCinemas_id +
                ", reservedAt=" + reservedAt +
                ", rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", user_id=" + user_id +
                '}';
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

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
