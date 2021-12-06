package com.mob3000.cinematrum.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.Category;
import com.mob3000.cinematrum.dataModels.Cinema;
import com.mob3000.cinematrum.dataModels.Hall;
import com.mob3000.cinematrum.dataModels.Movie;
import com.mob3000.cinematrum.dataModels.MoviesCinemas;
import com.mob3000.cinematrum.dataModels.Ticket;
import com.mob3000.cinematrum.dataModels.User;
import com.mob3000.cinematrum.dataModels.Wishlist;
import com.mob3000.cinematrum.helpers.Validator;

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
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_username);
                int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_email);
                int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
                int saltIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_salt);
                int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephone);
                int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);
                tmpUser.setUser_id(cursor.getInt((idIndex)));
                tmpUser.setUserType(cursor.getString(userTypeIndex));
                tmpUser.setName(cursor.getString(nameIndex));
                tmpUser.setEmail(cursor.getString(emailIndex));
                tmpUser.setPasswordHash(cursor.getString(passwordIndex));
                tmpUser.setSalt(cursor.getString(saltIndex));
                tmpUser.setTelephone(cursor.getString(telephoneIndex));
                tmpUser.setWishlist(getWishlists(ctx, DatabaseHelper.COLUMN_WISHLIST_userId, String.valueOf(tmpUser.getUser_id())));
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
            String sql;
            Cursor cursor;

            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + " where " + selectColumn + "=?;";
                String[] sqlArgs = new String[]{selectValue};
                cursor = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + ";";
                cursor = db.rawQuery(sql, null);
            }

            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userID);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_username);
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_email);
            int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
            int saltIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_salt);
            int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephone);
            int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);

            if (cursor.moveToFirst()) {
                do {
                    User tmpUser = new User();

                    tmpUser.setUser_id(cursor.getInt((idIndex)));
                    tmpUser.setUserType(cursor.getString(userTypeIndex));
                    tmpUser.setName(cursor.getString(nameIndex));
                    tmpUser.setEmail(cursor.getString(emailIndex));
                    tmpUser.setPasswordHash(cursor.getString(passwordIndex));
                    tmpUser.setSalt(cursor.getString(saltIndex));
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

    public static ArrayList<Ticket> getTickets(Context ctx, String selectColumn, String selectValue) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql;
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

            String sql;
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
                    tmpWishlist.setWishlist_id(c.getInt(indexWishlistId));
                    tmpWishlist.setUser_id(c.getInt(indexUserId));
                    tmpWishlist.setMovie_id(c.getInt(indexMovieId));

                    ArrayList<Movie> wishlistMovies = getMovies(ctx, DatabaseHelper.COLUMN_MOVIE_movieId, String.valueOf(tmpWishlist.getMovie_id()));
                    if (wishlistMovies.size() == 1)
                        tmpWishlist.set_movie(wishlistMovies.get(0));
                    else
                        tmpWishlist.set_movie(new Movie());

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

            String sql;
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

    public static ArrayList<MoviesCinemas> getMoviesCinemas(Context ctx, String selectColumn, String selectValue) {
        ArrayList<MoviesCinemas> moviesCinemas = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);
        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            String sql;
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

    public static ArrayList<MoviesCinemas> getMoviesCinemasByCinemaId(Context ctx, int movieId, int cinemaId) {
        ArrayList<MoviesCinemas> moviesCinemas = new ArrayList();
        try {

            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            long currentUnixTime = System.currentTimeMillis() / 1000;

            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_HALL + " on " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_hallId
                    + " = " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_hallId
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_CINEMA + " on " + DatabaseHelper.TABLENAME_CINEMA + "." + DatabaseHelper.COLUMN_CINEMA_cinemaId
                    + " = " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_hallId
                    + " where " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID + "=? AND "
                    + DatabaseHelper.TABLENAME_CINEMA + "." + DatabaseHelper.COLUMN_CINEMA_cinemaId + " =?"
                    + " AND " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_date + ">=?"
                    + " ORDER BY " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS +"." + DatabaseHelper.COLUMN_MOVIESCINEMAS_date
                    + ";";
            String[] sqlArgs = new String[]{String.valueOf(movieId), String.valueOf(cinemaId), String.valueOf(currentUnixTime)};

            Cursor c = db.rawQuery(sql, sqlArgs);

            if (c.moveToFirst()) {
                int indexMoviesCinemaId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_moviesCinemasID);
                int indexMovieId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID);
                int indexHallId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_hallId);
                int indexPrice = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_price);
                int indexSeatsAvailable = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_seatsAvailable);
                int indexAllSeats = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_allSeats);
                int indexDate = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIESCINEMAS_date);

                do {
                    MoviesCinemas tmpMoviesCinema = new MoviesCinemas();
                    tmpMoviesCinema.setMoviesCinemas_id(c.getInt(indexMoviesCinemaId));
                    tmpMoviesCinema.setMovie_id(c.getInt(indexMovieId));
                    tmpMoviesCinema.setHall_id(c.getInt(indexHallId));
                    tmpMoviesCinema.setPrice(c.getDouble(indexPrice));
                    int allSeats = c.getInt(indexAllSeats);
                    ArrayList<Ticket> tickets = getTickets(ctx, DatabaseHelper.COLUMN_TICKET_moviesCinemaID, String.valueOf(tmpMoviesCinema.getMoviesCinemas_id()));
                    int seatsAvailable = allSeats - tickets.size();
                    tmpMoviesCinema.setSeatsAvailable(seatsAvailable);

                    int unixTimestamp = c.getInt(indexDate);
                    tmpMoviesCinema.setDate((new java.util.Date((long) unixTimestamp * 1000)));
                    moviesCinemas.add(tmpMoviesCinema);
                }
                while (c.moveToNext());
            }
            c.close();
            db.close();
            return moviesCinemas;
        } catch (Exception e) {
            return moviesCinemas;
        }
    }

    //TODO check this method
    public static ArrayList<Cinema> getCinemas(Context ctx, String selectColumn, String selectValue) {
        ArrayList<Cinema> cinemas = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            String sql;
            Cursor c;

            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_CINEMA + " where " + selectColumn + "=?";
                String[] sqlArgs = new String[]{selectValue};
                c = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_CINEMA + ";";
                c = db.rawQuery(sql, null);
            }

            if (c.moveToFirst()) {
                int indexCinemaId = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_cinemaId);
                int indexName = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_name);
                int indexLongitude = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_longitude);
                int indexLatitude = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_latitude);
                do {
                    Cinema tmpCinema = new Cinema();
                    tmpCinema.setCinema_id(c.getInt(indexCinemaId));
                    tmpCinema.setName(c.getString(indexName));
                    tmpCinema.setLatitude(c.getFloat(indexLatitude));
                    tmpCinema.setLongitude(c.getFloat(indexLongitude));
                    //TODO load halls with own function like GetHalls
                    cinemas.add(tmpCinema);
                }
                while (c.moveToNext());
            }
            c.close();
            db.close();
            return cinemas;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return cinemas;
        }
    }


    // returns null if user is not logged in or userId is not found.
    @Deprecated
    public static User getLoggedInUser(Context ctx) {
        // Get Userid out of UserLoggedIn
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_USER + ";";

            Cursor c = db.rawQuery(sql, null);
            if (c.getCount() < 1) return null;

            c.moveToFirst();
            int indexUserIndex = c.getColumnIndex(DatabaseHelper.COLUMN_LOGGEDINUSER_userId);
            int userID = c.getInt(indexUserIndex);

            User user = getSingleUser(ctx, userID);
            //if (userList.size() == 1) return userList.get(0);


            c.close();
            db.close();

            return user;


        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return null;
        }
    }

    public static boolean addMovieToWishlist(Context ctx, int userId, int movieId) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_WISHLIST_userId, userId);
            values.put(DatabaseHelper.COLUMN_WISHLIST_movieId, movieId);

            long rowIndex = db.insert(DatabaseHelper.TABLENAME_WISHLIST, null, values);
            db.close();

            return rowIndex != -1;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    public static boolean removeMovieFromWishlist(Context ctx, int userId, int movieId) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_WISHLIST_userId, userId);
            values.put(DatabaseHelper.COLUMN_WISHLIST_movieId, movieId);

            String deleteCondition = DatabaseHelper.COLUMN_WISHLIST_movieId + " = ? and " + DatabaseHelper.COLUMN_WISHLIST_userId + " = ? ";
            String[] deleteArgs = new String[]{String.valueOf(movieId), String.valueOf(userId)};

            int rowsDeleted = db.delete(DatabaseHelper.TABLENAME_WISHLIST, deleteCondition, deleteArgs);

            db.close();
            return rowsDeleted > 0;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    public static boolean insertTicket(Context ctx, Ticket ticket) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TICKET_userID, ticket.getUser_id());
            values.put(DatabaseHelper.COLUMN_TICKET_moviesCinemaID, ticket.getMoviesCinemas_id());

            long currentUnixTime = System.currentTimeMillis();

            values.put(DatabaseHelper.COLUMN_TICKET_reservedAt, (long) currentUnixTime / 1000);
            values.put(DatabaseHelper.COLUMN_TICKET_rowNumber, ticket.getRowNumber());
            values.put(DatabaseHelper.COLUMN_TICKET_seatNumber, ticket.getSeatNumber());

            long rowInserted = db.insert(DatabaseHelper.TABLENAME_TICKET, null, values);
            return rowInserted != -1;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    public static boolean insertUser(Context ctx, User u) throws EmailTakenException {
        try {

            ArrayList<User> userWithSameEmail = DataAcessor.getUser(ctx, DatabaseHelper.COLUMN_USER_email, u.getEmail());
            if (userWithSameEmail.size() > 0)
                throw new EmailTakenException("Email -> " + u.getEmail() + " already in use. Try again!");

            ContentValues values = new ContentValues();

            // Salt and HashPassword added
            values.put(DatabaseHelper.COLUMN_USER_username, u.getName());
            values.put(DatabaseHelper.COLUMN_USER_email, u.getEmail());
            values.put(DatabaseHelper.COLUMN_USER_password, u.getPasswordHash());
            values.put(DatabaseHelper.COLUMN_USER_salt, u.getSalt());
            values.put(DatabaseHelper.COLUMN_USER_userType, u.getUserType());
            values.put(DatabaseHelper.COLUMN_USER_telephone, u.getTelephone());


            return insertData(ctx, values, DatabaseHelper.TABLENAME_USER);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    public static boolean checkUserCredentials(Context ctx, User u) {
        try {
            // Get user by userId from Database
            ArrayList<User> dbUsers = getUser(ctx, DatabaseHelper.COLUMN_USER_email, u.getEmail());
            if (dbUsers.size() != 1) return false;

            String hashedPasswordWithoutSalt = Validator.ExtractPasswordPart(dbUsers.get(0).getPasswordHash());
            return hashedPasswordWithoutSalt.equals(u.getPasswordHash());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    private static boolean insertData(Context ctx, ContentValues values, String tableName) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            long rowInserted = db.insert(tableName, null, values);
            db.close();

            return rowInserted != -1;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }


    public static boolean updateUser(Context ctx, User u) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER_username, u.getName());
            values.put(DatabaseHelper.COLUMN_USER_email, u.getEmail());
            values.put(DatabaseHelper.COLUMN_USER_telephone, u.getTelephone());
            values.put(DatabaseHelper.COLUMN_USER_userType, u.getUserType());

            long rowsUpdated = db.update(DatabaseHelper.TABLENAME_USER, values, DatabaseHelper.COLUMN_USER_userID + "=?", new String[]{String.valueOf(u.getUser_id())});
            return rowsUpdated == 1;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    public static boolean logInUser(Context ctx, User u) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            // delete User if log out was not succesful

            int rowsDeleted = db.delete(DatabaseHelper.TABLENAME_LOGGEDINUSER, null, null);

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_LOGGEDINUSER_userId, u.getUser_id());

            long insertedRow = db.insert(DatabaseHelper.TABLENAME_LOGGEDINUSER, null, values);

            db.close();
            return insertedRow != -1;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    @Deprecated
    public static boolean logOutUser(Context ctx, User u) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_LOGGEDINUSER_userId, u.getUser_id());

            String deleteCondition = DatabaseHelper.COLUMN_LOGGEDINUSER_userId + " = ?";
            String[] deleteArgs = new String[]{String.valueOf(u.getUser_id())};

            int rowsDeleted = db.delete(DatabaseHelper.TABLENAME_LOGGEDINUSER, deleteCondition, deleteArgs);

            db.close();
            return rowsDeleted > 0;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }


    // TODO: Refactor: Load only movies in future based on moviesCinemas IMPORTANT
    public static ArrayList<Movie> getMovies(Context ctx, String selectColumn, String selectValue) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            String sql;
            Cursor c;

            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIE + " where " + selectColumn + "=?";
                String[] sqlArgs = new String[]{selectValue};
                c = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIE + ";";
                c = db.rawQuery(sql, null);
            }

            if (c.moveToFirst()) {
                int indexMovieId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_movieId);
                int indexName = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_name);
                int indexPicture = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_picture);
                int indexPlublishedDate = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_publishedDate);
                int indexDescription = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_description);
                int indexRating = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_rating);
                //Denis 7.10.2021
                int indexMovieTrailerURL = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_movieTrailerURL);
                int indexDuration = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_duration);


                do {
                    Movie tmpMovie = new Movie();
                    tmpMovie.setMovie_id(c.getInt(indexMovieId));
                    tmpMovie.setName(c.getString(indexName));
                    tmpMovie.setDescription(c.getString(indexDescription));
                    tmpMovie.setPicture(c.getString(indexPicture));
                    int unixTimestamp = c.getInt(indexPlublishedDate);
                    tmpMovie.setPublishedDate((new java.util.Date((long) unixTimestamp * 1000)));
                    tmpMovie.setMoviesCinemas(getMoviesCinemas(ctx, DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID, String.valueOf(tmpMovie.getMovie_id())));
                    tmpMovie.setCategories(getCategoriesForMovie(ctx, tmpMovie.getMovie_id()));
                    tmpMovie.setCategoriesNamesConcat(concatCategoryNames(tmpMovie.getCategories()));
                    tmpMovie.setRating(c.getString(indexRating));
                    tmpMovie.setDuration(c.getString(indexDuration));
                    //Denis 7.10.2021
                    tmpMovie.setMovieTrailerURL(c.getString(indexMovieTrailerURL));
                    movies.add(tmpMovie);
                }
                while (c.moveToNext());
            }

            c.close();
            db.close();
            return movies;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return movies;
        }
    }

    public static ArrayList<Cinema> getCinemasForMovie(Context ctx, int movieId) {
        ArrayList<Cinema> cinemas = new ArrayList<>();

        try {

            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            // Load all CinemaMovies by movieId grouped by cinemaId
            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS
                    + " LEFT JOIN "  + DatabaseHelper.TABLENAME_HALL
                    + " on " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_hallId + " = " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_hallId
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_CINEMA
                    + " on " + DatabaseHelper.TABLENAME_CINEMA + "." + DatabaseHelper.COLUMN_CINEMA_cinemaId + " = " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_cinemaId
                    + " where " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID + "=?"
                    + " group by " + DatabaseHelper.TABLENAME_CINEMA + "." + DatabaseHelper.COLUMN_CINEMA_cinemaId + ";";
            String[] sqlArgs = new String[]{String.valueOf(movieId)};

            Cursor c = db.rawQuery(sql, sqlArgs);

            if (c.moveToFirst()) {

                int indexCinemaId = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_cinemaId);
                int indexName = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_name);
                int indexLatitude = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_latitude);
                int indexLongitude = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_longitude);


                do {
                    Cinema tmpCinema = new Cinema();
                    tmpCinema.setCinema_id(c.getInt(indexCinemaId));
                    tmpCinema.setName(c.getString(indexName));
                    tmpCinema.setLatitude(c.getFloat(indexLatitude));
                    tmpCinema.setLongitude(c.getFloat(indexLongitude));
                    cinemas.add(tmpCinema);

                } while (c.moveToNext());
            }
            return cinemas;
        } catch (Exception ex) {
            ex.printStackTrace();
            return cinemas;
        }
    }


    public static ArrayList<Cinema> getCinemasForMovieFromLocation(Context ctx, Location location, int movieId, int radius) {
        ArrayList<Cinema> cinemas = new ArrayList<>();

        try {

            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            // Load all CinemaMovies by movieId grouped by cinemaId
            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS
                    + " LEFT JOIN "  + DatabaseHelper.TABLENAME_HALL
                    + " on " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_hallId + " = " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_hallId
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_CINEMA
                    + " on " + DatabaseHelper.TABLENAME_CINEMA + "." + DatabaseHelper.COLUMN_CINEMA_cinemaId + " = " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_cinemaId
                    + " where " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID + "=?"
                    + " group by " + DatabaseHelper.TABLENAME_CINEMA + "." + DatabaseHelper.COLUMN_CINEMA_cinemaId + ";";
            String[] sqlArgs = new String[]{String.valueOf(movieId)};

            Cursor c = db.rawQuery(sql, sqlArgs);

            if (c.moveToFirst()) {

                int indexCinemaId = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_cinemaId);
                int indexName = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_name);
                int indexLatitude = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_latitude);
                int indexLongitude = c.getColumnIndex(DatabaseHelper.COLUMN_CINEMA_longitude);

                ArrayList<Cinema> allCinemas = new ArrayList<>();

                do {
                    Cinema tmpCinema = new Cinema();
                    tmpCinema.setCinema_id(c.getInt(indexCinemaId));
                    tmpCinema.setName(c.getString(indexName));
                    tmpCinema.setLatitude(c.getFloat(indexLatitude));
                    tmpCinema.setLongitude(c.getFloat(indexLongitude));
                    allCinemas.add(tmpCinema);

                } while (c.moveToNext());

                //Compare distances
                for (Cinema cinema : allCinemas) {
                    Location cinemaLocation = new Location("tmpLocation");
                    cinemaLocation.setLatitude(cinema.getLatitude());
                    cinemaLocation.setLongitude(cinema.getLongitude());
                    float distance = cinemaLocation.distanceTo(location) / 1000; // distance in km;
                    if (distance <= radius) {
                        cinemas.add(cinema);
                    }
                }
            }
            return cinemas;
        } catch (Exception ex) {
            ex.printStackTrace();
            return cinemas;
        }
    }

    public static ArrayList<Movie> getMoviesFromLocation(Context ctx, Location location, int radius) {

        ArrayList<Movie> finalResult = new ArrayList<>();
        try {
            // Load all cinemas
            ArrayList<Cinema> allCinemas = getCinemas(ctx, "", "");
            ArrayList<Cinema> cinemasInRadius = new ArrayList<>();

            // select cinemas by radius
            String sqlInStatement = "";
            int counter = 0;
            for (Cinema c : allCinemas) {
                Location cinemaLocation = new Location("tmpLocation");
                cinemaLocation.setLatitude(c.getLatitude());
                cinemaLocation.setLongitude(c.getLongitude());
                float distance = cinemaLocation.distanceTo(location) / 1000; // distance in km;
                if (distance <= radius) {
                    //cinemasInRadius.add(c);
                    if (counter > 0)
                        sqlInStatement += "," + c.getCinema_id();
                    else
                        sqlInStatement += String.valueOf(c.getCinema_id());

                    counter++;
                }

            }

            // no cinema found => no movie in given radius
            if (sqlInStatement == "")
                return finalResult;

            // select movieCinemas with movieId, Join movie, Group by movieId
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_MOVIE
                    + " on " + DatabaseHelper.TABLENAME_MOVIE + "." + DatabaseHelper.COLUMN_MOVIE_movieId + " = " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_HALL
                    + " on " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_hallId + " = "+ DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_hallId
                    + " where " + DatabaseHelper.TABLENAME_HALL + "." + DatabaseHelper.COLUMN_HALL_cinemaId + " in (" + sqlInStatement + ") AND " + DatabaseHelper.TABLENAME_MOVIES_CINEMAS + "." + DatabaseHelper.COLUMN_MOVIESCINEMAS_date + " > strftime('%s', 'now') "
                    + " group by " + DatabaseHelper.TABLENAME_MOVIE + "." + DatabaseHelper.COLUMN_MOVIE_movieId + ";";
            Cursor c = db.rawQuery(sql, null);

            if (c.moveToFirst()) {


                int indexMovieId = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_movieId);
                int indexName = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_name);
                int indexPicture = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_picture);
                int indexPlublishedDate = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_publishedDate);
                int indexDescription = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_description);
                int indexRating = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_rating);
                int indexDuration = c.getColumnIndex(DatabaseHelper.COLUMN_MOVIE_duration);


                do {
                    Movie tmpMovie = new Movie();
                    tmpMovie.setMovie_id(c.getInt(indexMovieId));
                    tmpMovie.setName(c.getString(indexName));
                    tmpMovie.setDescription(c.getString(indexDescription));
                    tmpMovie.setPicture(c.getString(indexPicture));
                    int unixTimestamp = c.getInt(indexPlublishedDate);
                    tmpMovie.setPublishedDate((new java.util.Date((long) unixTimestamp * 1000)));
                    tmpMovie.setMoviesCinemas(getMoviesCinemas(ctx, DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID, String.valueOf(tmpMovie.getMovie_id())));
                    tmpMovie.setCategories(getCategoriesForMovie(ctx, tmpMovie.getMovie_id()));
                    tmpMovie.setCategoriesNamesConcat(concatCategoryNames(tmpMovie.getCategories()));
                    tmpMovie.setRating(c.getString(indexRating));
                    tmpMovie.setDuration(c.getString(indexDuration));
                    finalResult.add(tmpMovie);
                }
                while (c.moveToNext());
            }

            c.close();
            db.close();

            return finalResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return finalResult;
        }
    }


    private static String concatCategoryNames(ArrayList<Category> categories) {
        String contactedNames = "";
        for (int i = 0; i < categories.size(); i++) {
            if (!TextUtils.isEmpty(categories.get(i).getName())) {
                if (i > 0) {
                    contactedNames += ", ";
                }
                contactedNames += categories.get(i).getName();
            }
        }
        return contactedNames;
    }

    // TODO FINISH!!

    public static ArrayList<Category> getCategories(Context ctx, String selectColumn, String selectValue) {

        ArrayList<Category> categories = new ArrayList<>();
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            String sql;
            Cursor c;

            if (selectColumn != "") {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_CATEGORIE + " where " + selectColumn + "=?;";
                String[] sqlArgs = new String[]{selectValue};
                c = db.rawQuery(sql, sqlArgs);
            } else {
                sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_CATEGORIE + ";";
                c = db.rawQuery(sql, null);
            }

            if (c.moveToFirst()) {
                int indexCategorieId = c.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_categoryId);
                int indexName = c.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_name);
                int indexImageUrl = c.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_imageUrl);

                do {
                    Category tmpCat = new Category();
                    tmpCat.setName(c.getString(indexName));
                    tmpCat.setCategory_id(c.getInt(indexCategorieId));
                    tmpCat.setImageUrl(c.getString(indexImageUrl));
                    // TODO: Load Movies with own function (like getMovies)
                    categories.add(tmpCat);

                } while (c.moveToNext());
            }

            c.close();
            db.close();

            return categories;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return categories;
        }
    }


    public static ArrayList<Category> getCategoriesForMovie(Context ctx, int movie_id) {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_CATEGORIE_MOVIE
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_CATEGORIE + " on " + DatabaseHelper.TABLENAME_CATEGORIE_MOVIE + "." + DatabaseHelper.COLUMN_CATEGORIESMOVIES_categoryId
                    + " = " + DatabaseHelper.TABLENAME_CATEGORIE + "." + DatabaseHelper.COLUMN_CATEGORY_categoryId
                    + " where " + DatabaseHelper.COLUMN_CATEGORIESMOVIES_movieId + "=?;";


            String[] sqlArgs = new String[]{String.valueOf(movie_id)};

            Cursor c = db.rawQuery(sql, sqlArgs);

            if (c.moveToFirst()) {

                int indexCategoryId = c.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_categoryId);
                int indexCategoryName = c.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_name);

                do {
                    Category tmpCat = new Category();
                    tmpCat.setCategory_id(c.getInt(indexCategoryId));
                    tmpCat.setName(c.getString(indexCategoryName));
                    // TODO: LOAD ALL MOVIES FROM ONE CATEGORIE (Needs one function!!)
                    categories.add(tmpCat);
                }
                while (c.moveToNext());
            }

            c.close();
            db.close();

            return categories;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return categories;
        }
    }

    public static ArrayList<String> getFreeRowsForMovieCinema(Context ctx, MoviesCinemas movieCinema) {
        ArrayList<String> freeRows = new ArrayList<>();
        try {
            // get Tickets for movieCinema
            ArrayList<Ticket> tickets = getTickets(ctx, DatabaseHelper.COLUMN_TICKET_moviesCinemaID, String.valueOf(movieCinema.getMoviesCinemas_id()));


            // get number of Rows for hall in movieCinema
            Hall hall = getHalls(ctx, DatabaseHelper.COLUMN_HALL_hallId, String.valueOf(movieCinema.getHall_id())).get(0);
            int numberOfRows = hall.getRows();
            int seatsPerRow = hall.getSeatsPerRow();

            for (int i = 0; i < numberOfRows; i++) {
                if (checkFreeSeatsInRow(tickets, i + 1, seatsPerRow))
                    freeRows.add(String.valueOf(i + 1));
            }

            return freeRows;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return freeRows;
        }
    }

    public static boolean checkFreeSeatsInRow(ArrayList<Ticket> tickets, int rowNumber, int seatsPerRow) {

        ArrayList<Ticket> ticketsInRow = getTicketsForRow(tickets, rowNumber);
        return ticketsInRow.size() < seatsPerRow;
    }

    public static ArrayList<String> getFreeSeatsForRow(Context ctx, MoviesCinemas movieCinema, int rowNumber) {
        ArrayList<String> freeSeats = new ArrayList<>();

        try {
            // get Tickets for movieCinema
            ArrayList<Ticket> tickets = getTickets(ctx, DatabaseHelper.COLUMN_TICKET_moviesCinemaID, String.valueOf(movieCinema.getMoviesCinemas_id()));
            ArrayList<Ticket> ticketsInRow = getTicketsForRow(tickets, rowNumber);

            Hall hall = getHalls(ctx, DatabaseHelper.COLUMN_HALL_hallId, String.valueOf(movieCinema.getHall_id())).get(0);
            int seatsPerRow = hall.getSeatsPerRow();

            for (int i = 0; i < seatsPerRow; i++) {
                // check if there is a ticket in row
                if (!checkSeatInTickets(ticketsInRow, i + 1))
                    freeSeats.add(String.valueOf(i + 1));
            }

            return freeSeats;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return freeSeats;
        }
    }

    private static ArrayList<Ticket> getTicketsForRow(ArrayList<Ticket> tickets, int rowNumber) {
        ArrayList<Ticket> filteredTickets = new ArrayList<>();

        for (Ticket t : tickets) {
            if (t.getRowNumber() == rowNumber)
                filteredTickets.add(t);
        }
        return filteredTickets;
    }

    private static boolean checkSeatInTickets(ArrayList<Ticket> tickets, int seatNumber) {
        for (Ticket t : tickets) {
            if (t.getSeatNumber() == seatNumber)
                return true;
        }
        return false;
    }


}
