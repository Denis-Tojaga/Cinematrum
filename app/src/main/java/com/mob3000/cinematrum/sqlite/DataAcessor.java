package com.mob3000.cinematrum.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.mob3000.cinematrum.dataModels.Category;
import com.mob3000.cinematrum.dataModels.Hall;
import com.mob3000.cinematrum.dataModels.Movie;
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
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_username);
                int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
                int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephone);
                int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);
                tmpUser.setUser_id(cursor.getInt((idIndex)));
                tmpUser.setUserType(cursor.getString(userTypeIndex));
                tmpUser.setName(cursor.getString(nameIndex));
                tmpUser.setPasswordHash(cursor.getString(passwordIndex));
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
            int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_password);
            int telephoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_telephone);
            int userTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_userType);

            if (cursor.moveToFirst()) {
                do {
                    User tmpUser = new User();

                    tmpUser.setUser_id(cursor.getInt((idIndex)));
                    tmpUser.setUserType(cursor.getString(userTypeIndex));
                    tmpUser.setName(cursor.getString(nameIndex));
                    tmpUser.setPasswordHash(cursor.getString(passwordIndex)); // TODO Update for Salt and hash
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

    private static ArrayList<MoviesCinemas> getMoviesCinemas(Context ctx, String selectColumn, String selectValue) {
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


            c.close();
            db.close();

            return null;

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

    public static boolean insertUser(Context ctx, User u) throws UserNameTakenException {
        try {


            ArrayList<User> userWithSameName = DataAcessor.getUser(ctx, DatabaseHelper.COLUMN_USER_name, u.getName());
            if (userWithSameName.size() > 0)
                throw new UserNameTakenException("Username " + u.getName() + " is already taken");


            // TODO: DONT SAVE PASSWORD BUT CREATE HASH VALUE!

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.COLUMN_USER_name, u.getName());
            values.put(DatabaseHelper.COLUMN_USER_password, u.getPasswordHash()); // TODO: Update for Salt and Hash
            values.put(DatabaseHelper.COLUMN_USER_telephon, u.getTelephone());
            values.put(DatabaseHelper.COLUMN_USER_userType, u.getUserType());

            return insertData(ctx, values, DatabaseHelper.TABLENAME_USER);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    public static boolean checkUserCredentials(Context ctx, User u) {
        try {

            // TODO CHECK HASH VALUE NOT PASSWORD STRING

            // Get user by userId from Database
            ArrayList<User> dbUsers = getUser(ctx, DatabaseHelper.COLUMN_USER_username, u.getName());
            if (dbUsers.size() != 1) return false;

            return dbUsers.get(0).getPasswordHash() == u.getPasswordHash();
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


    // TODO: FINISH
    public static boolean updateUser(Context ctx, User u) {
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER_name, u.getName());
            values.put(DatabaseHelper.COLUMN_USER_telephon, u.getTelephone());
            values.put(DatabaseHelper.COLUMN_USER_userType, u.getUserType());

            long rowsUpdated = db.update(DatabaseHelper.TABLENAME_USER, values, DatabaseHelper.COLUMN_USER_userID + "=?", new String[]{String.valueOf(u.getUser_id())});
            return rowsUpdated == 1;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    // TODO: FINISH;
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

                do {
                    Movie tmpMovie = new Movie();
                    tmpMovie.setMovie_id(c.getInt(indexMovieId));
                    tmpMovie.setName(c.getString(indexName));
                    tmpMovie.setPicture(c.getString(indexPicture));
                    int unixTimestamp = c.getInt(indexPlublishedDate);
                    tmpMovie.setPublishedDate((new java.util.Date((long) unixTimestamp * 1000)));
                    tmpMovie.setMoviesCinemas(getMoviesCinemas(ctx, DatabaseHelper.COLUMN_MOVIESCINEMAS_movieID, String.valueOf(tmpMovie.getMovie_id())));
                    tmpMovie.setCategories(getCategoriesForMovie(ctx, tmpMovie.getMovie_id()));
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

                do {
                    Category tmpCat = new Category();
                    tmpCat.setName(c.getString(indexName));
                    tmpCat.setCategorie_id(c.getInt(indexCategorieId));
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


    // TODO : FINISH!!!
    public static ArrayList<Category> getCategoriesForMovie(Context ctx, int movie_id) {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            DatabaseHelper dbhelper = new DatabaseHelper(ctx);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            String sql = "SELECT * FROM " + DatabaseHelper.TABLENAME_CATEGORIE_MOVIE
                    + " LEFT JOIN " + DatabaseHelper.TABLENAME_CATEGORIE + " on " + DatabaseHelper.TABLENAME_CATEGORIE_MOVIE + "." + DatabaseHelper.COLUMN_CATEGORIESMOVIES_movieId
                    + " = " + DatabaseHelper.TABLENAME_CATEGORIE + "." + DatabaseHelper.COLUMN_CATEGORY_categoryId
                    + " where " + DatabaseHelper.COLUMN_CATEGORIESMOVIES_movieId + "=?;";
            String[] sqlArgs = new String[]{String.valueOf(movie_id)};

            Cursor c = db.rawQuery(sql, sqlArgs);

            if (c.moveToFirst()) {

                int indexCategoryId = c.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_categoryId);
                int indexCategoryName = c.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_name);

                do {
                    Category tmpCat = new Category();
                    tmpCat.setCategorie_id(c.getInt(indexCategoryId));
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


}
