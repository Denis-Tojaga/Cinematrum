package com.mob3000.cinematrum.sqlite;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.Hall;
import com.mob3000.cinematrum.dataModels.MoviesCinemas;
import com.mob3000.cinematrum.dataModels.Ticket;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.dataModels.Wishlist;

import java.util.ArrayList;


public class DataAcessor {

    private static final String LOG_TAG = "DATAACESSOR";

    private DataAcessor() {
    }

    public static User getSingleUser(Context ctx, int userId) {
        User user = new User();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + " where " + DatabaseHelper.COLUMN_USER_userID + "=?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {

                User tmpUser = new User();
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_name);
                int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
                int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephon);
                int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);
                tmpUser.setUser_id(cursor.getInt((idIndex)));
                tmpUser.setUserType(cursor.getString(userTypeIndex));
                tmpUser.setName(cursor.getString(nameIndex));
                tmpUser.setPassword(cursor.getString(passwordIndex));
                tmpUser.setTelephone(cursor.getString(telephoneIndex));
                user = tmpUser;

            }
            cursor.close();
            db.close();
            return user;
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
            return user;
        }
    }


    public static ArrayList<User> getUser(Context ctx, String selectColumn, String selectValue) {
        ArrayList<User> allUsers = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);
        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "";
            Cursor cursor;

            if (selectColumn != ""){
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + " where " + selectColumn + "=?;";
                String[] sqlArgs = new String[]{selectValue};
                 cursor = db.rawQuery(sql, sqlArgs);
            }
            else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + " ;";
                cursor = db.rawQuery(sql, null);
            }

            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userID);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_name);
            int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
            int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephon);
            int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);

            if (cursor.moveToFirst()) {
                do {
                    User tmpUser = new User();

                    tmpUser.setUser_id(cursor.getInt((idIndex)));
                    tmpUser.setUserType(cursor.getString(userTypeIndex));
                    tmpUser.setName(cursor.getString(nameIndex));
                    tmpUser.setPassword(cursor.getString(passwordIndex));
                    tmpUser.setTelephone(cursor.getString(telephoneIndex));
                    tmpUser.setTickets(getTickets(ctx, DatabaseHelper.COLUMN_TICKET_userID, String.valueOf(tmpUser.getUser_id())));
                    tmpUser.setWishlist(getWishlists(ctx, DatabaseHelper.COLUMN_WISHLIST_userId, String.valueOf(tmpUser.getUser_id())));
                    Log.d("DATAACESSOR", tmpUser.toString());
                    allUsers.add(tmpUser);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return allUsers;
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
            return allUsers;
        }
    }

    private static ArrayList<Ticket> getTickets(Context ctx, String selectColumn, String selectValue) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "";
            Cursor c;

            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_TICKET + " where " + selectColumn + " =?";
                String[] sqlArgs = new String[]{selectValue};
                c = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_TICKET + ";";
                c = db.rawQuery(sql, null);
            }

            int ticketIdIndex = c.getColumnIndex(DatabaseHelper.COLUMN_TICKET_ticketID);
            int userIdIndex = c.getColumnIndex(DatabaseHelper.COLUMN_TICKET_userID);
            int moviesCinemasIndex = c.getColumnIndex(DatabaseHelper.COLUMN_TICKET_moviesCinemaID);
            int reservedAtIndex = c.getColumnIndex(DatabaseHelper.COLUMN_TICKET_reservedAt);
            int rowNumberIndex = c.getColumnIndex(DatabaseHelper.COLUMN_TICKET_rowNumber);
            int seatNumberIndex = c.getColumnIndex(DatabaseHelper.COLUMN_TICKET_seatNumber);

            if (c.moveToFirst()) {
                do {
                    Ticket tmpTicket = new Ticket();
                    tmpTicket.setTicket_id(c.getInt(ticketIdIndex));
                    tmpTicket.setUser_id(c.getInt(userIdIndex));
                    tmpTicket.setMoviesCinemas_id(c.getInt(moviesCinemasIndex));
                    int unixTimestamp = c.getInt(reservedAtIndex);
                    tmpTicket.setReservedAt(new java.util.Date((long) unixTimestamp * 1000));
                    tmpTicket.setRowNumber(c.getInt((rowNumberIndex)));
                    tmpTicket.setSeatNumber(c.getInt(seatNumberIndex));
                    tickets.add(tmpTicket);
                }
                while (c.moveToNext());
            }
            Log.d("DATAACESSOR", "COUNT TICKETS  User " + selectValue + ": " + tickets.size());

            c.close();
            db.close();
            return tickets;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return tickets;
        }
    }

    private static ArrayList<Wishlist> getWishlists(Context ctx, String selectColumn, String selectValue) {
        ArrayList<Wishlist> wishlists = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            String sql = "";
            Cursor c;

            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_WISHLIST + " where " + selectColumn + "=?";
                String[] sqlArgs = new String[]{selectValue};
                c = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_WISHLIST + ";";
                c = db.rawQuery(sql, null);
            }

            if (c.moveToFirst()) {

                int indexWishlistId = c.getColumnIndex(DatabaseHelper.COLUMN_WISHLIST_wishlistId);
                int indexUserId = c.getColumnIndex(DatabaseHelper.COLUMN_WISHLIST_userId);
                int indexMovieId = c.getColumnIndex(DatabaseHelper.COLUMN_WISHLIST_movieId);
                int indexHallId = c.getColumnIndex(DatabaseHelper.COLUMN_WISHLIST_hallId); // TODO Do we need this?
                do {
                    Wishlist tmpWishlist = new Wishlist();
                    tmpWishlist.setWishilst_id(c.getInt(indexWishlistId));
                    tmpWishlist.setUser_id(c.getInt(indexUserId));
                    tmpWishlist.setMovie_id(c.getInt(indexMovieId));
                    wishlists.add(tmpWishlist);
                }
                while (c.moveToNext());
            }
            Log.d("DATAACESSOR", "COUNT WISHLIST  User " + selectValue + ": " + wishlists.size());
            c.close();
            db.close();
            return wishlists;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return wishlists;
        }
    }

    public static ArrayList<Hall> getHalls(Context ctx, String selectColumn, String selectValue) {
        ArrayList<Hall> halls = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            String sql = "";
            Cursor c;

            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_HALL + " where " + selectColumn + "=?";
                String[] sqlArgs = new String[]{selectValue};
                c = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_HALL + ";";
                c = db.rawQuery(sql, null);
            }

            if (c.moveToFirst()) {
                int indexHallId = c.getColumnIndex(DatabaseHelper.COLUMN_HALL_hallId);
                int indexCinemaId = c.getColumnIndex(DatabaseHelper.COLUMN_HALL_cinemaId);
                int indexRows = c.getColumnIndex(DatabaseHelper.COLUMN_HALL_rows);
                int indexSeatRows = c.getColumnIndex(DatabaseHelper.COLUMN_HALL_seatsPerRow);
                do {
                    Hall tmpHall = new Hall();
                    tmpHall.setHall_id(c.getInt(indexHallId));
                    tmpHall.setCinema_id(c.getInt(indexCinemaId));
                    tmpHall.setRows(c.getInt(indexRows));
                    tmpHall.setSeatsPerRow(c.getInt(indexSeatRows));
                    tmpHall.setMoviesCinemas(getMoviesCinemas(ctx, DatabaseHelper.COLUMN_MOVIESCINEMAS_hallId, String.valueOf(tmpHall.getHall_id())));
                    halls.add(tmpHall);
                }
                while (c.moveToNext());
            }
            c.close();
            db.close();
            return halls;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return halls;
        }
    }

    private static ArrayList<MoviesCinemas> getMoviesCinemas(Context ctx, String selectColumn, String selectValue) {
        ArrayList<MoviesCinemas> moviesCinemas = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);
        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            String sql = "";
            Cursor c;
            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + " where " + selectColumn + "=?";
                String[] sqlArgs = new String[]{selectValue};
                c = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + ";";
                c = db.rawQuery(sql, null);
            }
            if (c.moveToFirst()) {
                int indexMoviesCinemasId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_moviesCinemasID);
                int indexMovieId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID);
                int indexHallId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_hallId);
                int indexPrice = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_price);
                int indexSeatsAvailable = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_seatsAvailable);
                int indexDate = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_date);
                do {
                    MoviesCinemas tmpMoviesCinema = new MoviesCinemas();
                    tmpMoviesCinema.setMoviesCinemas_id(c.getInt(indexMoviesCinemasId));
                    tmpMoviesCinema.setMovie_id(c.getInt(indexMovieId));
                    tmpMoviesCinema.setHall_id(c.getInt(indexHallId));
                    tmpMoviesCinema.setPrice(c.getDouble(indexPrice));
                    tmpMoviesCinema.setSeatsAvailable(c.getInt(indexSeatsAvailable));
                    int unixTimestamp = c.getInt(indexDate);
                    tmpMoviesCinema.setDate(new java.util.Date((long) unixTimestamp * 1000));
                    tmpMoviesCinema.setTickets(getTickets(ctx, DatabaseHelper.COLUMN_TICKET_moviesCinemaID, String.valueOf(tmpMoviesCinema.getMoviesCinemas_id())));
                    moviesCinemas.add(tmpMoviesCinema);
                }
                while (c.moveToNext());
            }
            c.close();
            db.close();
            return moviesCinemas;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return moviesCinemas;
        }
    }

    // returns null if user is not logged in or userId is not found.
    public static User getLoggedInUser(Context ctx) {
        // Get Userid out of UserLoggedIn
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_LOGGEDINUSER + ";";
            Cursor c = db.rawQuery(sql, null);
            if (c.getCount() < 1) return null;

            c.moveToFirst();
            int indexUserIndex = c.getColumnIndex(DatabaseHelper.COLUMN_LOGGEDINUSER_userId);
            int userID = c.getInt(indexUserIndex);

            ArrayList<User> userList = getUser(ctx, DatabaseHelper.COLUMN_USER_userID, String.valueOf(userID));
            if (userList.size() == 1) return userList.get(0);

            return null;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return null;
        }
    }
}
