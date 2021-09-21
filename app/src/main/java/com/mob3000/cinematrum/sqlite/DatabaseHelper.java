package com.mob3000.cinematrum.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    /* See example: http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/*/

    // Table Names
    public static final String TABLENAME_TICKET = "ticket";
    public static final String TABLENAME_USER = "user";
    public static final String TABLENAME_CINEMA = "cinema";
    public static final String TABLENAME_MOVIE = "movie";
    public static final String TABLENAME_MOVIES_CINEMAS = "moviesCinemas";
    public static final String TABLENAME_HALL = "hall";
    public static final String TABLENAME_CATEGORIE = "categorie";
    public static final String TABLENAME_CATEGORIE_MOVIE = "categorieMovie";
    public static final String TABLENAME_WISHLIST = "wishlist";
    public static final String TABLENAME_LOGGEDINUSER = "loggedinuser";
    // Columns - TICKET
    public static final String COLUMN_TICKET_ticketID = "ticket_id";
    public static final String COLUMN_TICKET_userID = "user_Id";
    public static final String COLUMN_TICKET_moviesCinemaID = "moviesCinema";
    public static final String COLUMN_TICKET_reservedAt = "reservedAt";
    public static final String COLUMN_TICKET_rowNumber = "rowNumber";
    public static final String COLUMN_TICKET_seatNumber = "seatNumber";
    // Columns - USER
    public static final String COLUMN_USER_userID = "user_id";
    public static final String COLUMN_USER_username = "name";
    public static final String COLUMN_USER_email = "email";
    public static final String COLUMN_USER_password = "password";
    public static final String COLUMN_USER_salt = "salt";
    public static final String COLUMN_USER_telephone = "telephon";
    public static final String COLUMN_USER_userType = "userType";

    public static final String DATABASE_NAME = "cinematrumDb.db";
    private static final int DATABASE_VERSION = 1;
    // COLUMNS - MOVIESCIENEMAS
    public static final String COLUMN_MOVIESCINEMAS_moviesCinemasID = "moviesCienemas_id";
    public static final String COLUMN_MOVIESCINEMAS_movieID = "movie_id";
    public static final String COLUMN_MOVIESCINEMAS_hallId = "hall_id";
    public static final String COLUMN_MOVIESCINEMAS_price = "price";
    public static final String COLUMN_MOVIESCINEMAS_seatsAvailable = "seatsAvailable";
    public static final String COLUMN_MOVIESCINEMAS_allSeats = "allSeats";
    public static final String COLUMN_MOVIESCINEMAS_date = "date";
    //    private static final String COLUMN_MOVIESCINEMAS_time = "time";
    // COLUMNS - HALL
    public static final String COLUMN_HALL_hallId = "hall_id";
    public static final String COLUMN_HALL_cinemaId = "cinema_id";
    public static final String COLUMN_HALL_rows = "rows";
    public static final String COLUMN_HALL_seatsPerRow = "seatsPerRow";
    // COLUMNS - CINEMA
    public static final String COLUMN_CINEMA_cinemaId = "cinema_id";
    public static final String COLUMN_CINEMA_name = "name";
    public static final String COLUMN_CINEMA_location = "location";
    // COLUMNS - WHISLIST
    public static final String COLUMN_WISHLIST_wishlistId = "wishlist_id";
    public static final String COLUMN_WISHLIST_userId = "user_id";
    public static final String COLUMN_WISHLIST_movieId = "movie_id";
    public static final String COLUMN_WISHLIST_hallId = "hall_id"; // TODO Why so we save the hall ID for a wishilist entry?
    // COLUMNS - CATEGORY
    public static final String COLUMN_CATEGORY_categoryId = "category_id";
    public static final String COLUMN_CATEGORY_name = "name";
    public static final String COLUMN_CATEGORY_unicodeIcon = "unicodeIcon";
    // COLUMNS - CATEGORIESMOVIES
    public static final String COLUMN_CATEGORIESMOVIES_categoryId = "category_id";
    public static final String COLUMN_CATEGORIESMOVIES_movieId = "movie_id";
    // COLUMNS - MOVIE
    public static final String COLUMN_MOVIE_movieId = "movie_id";
    public static final String COLUMN_MOVIE_name = "name";
    public static final String COLUMN_MOVIE_picture = "picture";
    public static final String COLUMN_MOVIE_video = "video";
    public static final String COLUMN_MOVIE_duration = "duration";
    public static final String COLUMN_MOVIE_description = "description";
    public static final String COLUMN_MOVIE_publishedDate = "publishedDate";
    // COLUMNS - LOGGEDINUSER
    public static final String COLUMN_LOGGEDINUSER_userId = "user_id";
    // INSERT DATA
    public static final String INSERT_MOVIE_TABLE = "INSERT INTO " + TABLENAME_MOVIE + " (" + COLUMN_MOVIE_name  + ", " + COLUMN_MOVIE_picture + ", " + COLUMN_MOVIE_video + ", " + COLUMN_MOVIE_duration + ", " + COLUMN_MOVIE_description + ", " + COLUMN_MOVIE_publishedDate + ") "

            + "VALUES (\"James Bond\",\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQy7G06j-HhGeHCUAiOwZU-gUcpvjlKnODkAJ4ZZPsaYVrAx8SKnuuQcsCboqU-WaDppNI&usqp=CAU\", \"\", 1.25, \"The hero James Bond likes to fight all the evil people all over the world. James Bond fights everyone while singing 'Hello' from Adele while walking on a train shooting all the bastards with his gun. At the end, the helicopter of the evil people crashes into his house, but he can escape through a hidden tunnel.\", 1631451990),"
            + "(\"IT\",\"https://akamai.vgc.no/v2/images/f2d2f165-41ec-4cbc-9fa7-01b69dbaf4e9?fit=crop&h=1267&w=1900&s=ca937e54cedd2e14493af218e2b695de02aae573\", \"\", 1.9, \"It is back. The clown who comes back every 25 years. Already two kids have been missing until the group of young children find out the truth\",1631451990)";
    public static final String INSERT_USER_TABLE = "INSERT INTO " + TABLENAME_USER + " (" + COLUMN_USER_username + ", " + COLUMN_USER_email + ", " + COLUMN_USER_password + ", " + COLUMN_USER_salt + ", " + COLUMN_USER_userType + ", " + COLUMN_USER_telephone + ") "
            + "VALUES (\"user1\",\"email1\",\"password1\",\"salt1\",01234, \"admin\"),"
            + "(\"user3\",\"email2\",\"password3\",\"salt2\",012348790809, \"user\"),"
            + "(\"user2\",\"email3\",\"password2\",\"salt3\",012348790809, \"user\");";
    public static final String INSERT_TICKET_TABLE = "INSERT INTO " + TABLENAME_TICKET + " (" + COLUMN_TICKET_userID + ", " + COLUMN_TICKET_moviesCinemaID + ", " + COLUMN_TICKET_reservedAt + ", " + COLUMN_TICKET_rowNumber + ", " + COLUMN_TICKET_seatNumber + ") "
            + "VALUES (1,1,1631451997,1,1),(1,1,1631451997,1,1);";
    public static final String INSERT_CINEMA_TABLE = "INSERT INTO " + TABLENAME_CINEMA + " (" + COLUMN_CINEMA_name + ", " + COLUMN_CINEMA_location + ") "
            + "VALUES (\"Hönefoss Cinema\", \"\"), (\"Ciname Oslo\", \"\"), (\"Oslo Theater\", \"\");";
    public static final String INSERT_HALL_TABLE = "INSERT INTO " + TABLENAME_HALL + " (" + COLUMN_HALL_cinemaId + ", " + COLUMN_HALL_rows + ", " + COLUMN_HALL_seatsPerRow + ") "
            + "VALUES (1,10,7), (2,8,20);";
    public static final String INSERT_WISHLIST_TABLE = "INSERT INTO " + TABLENAME_WISHLIST + " ( " + COLUMN_WISHLIST_userId + ", " + COLUMN_WISHLIST_movieId + ", " + COLUMN_WISHLIST_hallId + ") "
            + "VALUES (1, 1,1), (1,2,1); ";
    public static final String INSERT_MOVIESCINEMAS_TABLE = "INSERT INTO " + TABLENAME_MOVIES_CINEMAS + " (" + COLUMN_MOVIESCINEMAS_movieID + ", " + COLUMN_MOVIESCINEMAS_hallId + ", " + COLUMN_MOVIESCINEMAS_price + ", " + COLUMN_MOVIESCINEMAS_seatsAvailable + ", " + COLUMN_MOVIESCINEMAS_allSeats + ", " + COLUMN_MOVIESCINEMAS_date + ") "
            + "VALUES (1,1,10.99,20,100,1631451990), "
            + "(2,1,20.99,20,100,1631451990),"
            + "(1,2,10.99,20,100,1631451990);";
    public static final String INSERT_CATEGORIES_TABLE = "INSERT INTO " + TABLENAME_CATEGORIE + " (" + COLUMN_CATEGORY_name + ", " + COLUMN_CATEGORY_unicodeIcon + ") "
            + "VALUES (\"Comedy\", \"U+1F600\"),(\"Horror\", \"U+1F636\"),(\"Action\", \"U+1F920\"),(\"Super Hero\",\"U+1F9BE\");";
    public static final String INSERT_CATEGORIESMOVIES_TABLE = "INSERT INTO " + TABLENAME_CATEGORIE_MOVIE + " (" + COLUMN_CATEGORIESMOVIES_categoryId + ", " + COLUMN_CATEGORIESMOVIES_movieId + ") "
            + "VALUES (1,1),(1,2), (2,1), (3,1);";
public static final String INSERT_USERLOGGEDIN_TABLE = "INSERT INTO " + TABLENAME_LOGGEDINUSER + " ( " + COLUMN_LOGGEDINUSER_userId + ")"
        + "VALUES (1);";
     /* CREATING DATABASE SCHEME*/
    private static final String CREATE_TICKET_TABLE = "CREATE TABLE " + TABLENAME_TICKET
            + " ( " + COLUMN_TICKET_ticketID + " INTEGER NOT NULL, "
            + COLUMN_TICKET_userID + " INTEGER, "
            + COLUMN_TICKET_moviesCinemaID + " INTEGER, "
            + COLUMN_TICKET_reservedAt + " INTEGER NOT NULL, " // Storing UNIX timestamp as int
            + COLUMN_TICKET_rowNumber + " INTEGER NOT NULL, "
            + COLUMN_TICKET_seatNumber + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_TICKET_ticketID + " AUTOINCREMENT), "
            + "FOREIGN KEY(" + COLUMN_TICKET_userID + ") REFERENCES " + TABLENAME_USER + "(" + COLUMN_USER_userID + "), "
            + "FOREIGN KEY(" + COLUMN_TICKET_moviesCinemaID + ") REFERENCES " + TABLENAME_MOVIES_CINEMAS + "(" + COLUMN_MOVIESCINEMAS_moviesCinemasID + "));";
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLENAME_USER
            + " ( " + COLUMN_USER_userID + " INTEGER NOT NULL, "
            + COLUMN_USER_username + " TEXT, "
            + COLUMN_USER_email + " TEXT, "
            + COLUMN_USER_password + " TEXT NOT NULL, "
            + COLUMN_USER_salt + " TEXT, "
            + COLUMN_USER_telephone + " TEXT, "
            + COLUMN_USER_userType + " TEXT, "
            + "PRIMARY KEY(" + COLUMN_USER_userID + " AUTOINCREMENT));";
    private static final String CREATE_MOVIESCIENEMAS_TABLE = "CREATE TABLE " + TABLENAME_MOVIES_CINEMAS
            + " ( " + COLUMN_MOVIESCINEMAS_moviesCinemasID + " INTEGER NOT NULL, "
            + COLUMN_MOVIESCINEMAS_movieID + " INTEGER, "
            + COLUMN_MOVIESCINEMAS_hallId + " INTEGER, "
            + COLUMN_MOVIESCINEMAS_price + " NUMERIC NOT NULL, "
            + COLUMN_MOVIESCINEMAS_seatsAvailable + " INTEGER, "
            + COLUMN_MOVIESCINEMAS_allSeats + " INTEGER, "
            + COLUMN_MOVIESCINEMAS_date + " INTEGER NOT NULL, " // STORING UNIX TIMESTAMP
            + "PRIMARY KEY(" + COLUMN_MOVIESCINEMAS_moviesCinemasID + " AUTOINCREMENT), "
            + "FOREIGN KEY(" + COLUMN_MOVIESCINEMAS_movieID + ") REFERENCES " + TABLENAME_MOVIE + "(" + COLUMN_MOVIE_movieId + "), "
            + "FOREIGN KEY(" + COLUMN_MOVIESCINEMAS_hallId + ") REFERENCES " + TABLENAME_HALL + "(" + COLUMN_HALL_hallId + "));";
    private static final String CREATE_HALL_TABLE = "CREATE TABLE " + TABLENAME_HALL + " ("
            + COLUMN_HALL_hallId + " INTEGER NOT NULL, "
            + COLUMN_HALL_cinemaId + " INTEGER, "
            + COLUMN_HALL_rows + " INTEGER NOT NULL, "
            + COLUMN_HALL_seatsPerRow + " INTEGER NOT NULL,"
            + "PRIMARY KEY(" + COLUMN_HALL_hallId + " AUTOINCREMENT), "
            + "FOREIGN KEY(" + COLUMN_HALL_cinemaId + ") REFERENCES " + TABLENAME_MOVIE + "(" + COLUMN_MOVIE_movieId + "));";
    private static final String CREATE_CINEMA_TABLE = "CREATE TABLE " + TABLENAME_CINEMA + " ( "
            + COLUMN_CINEMA_cinemaId + " INTEGER NOT NULL, "
            + COLUMN_CINEMA_name + " TEXT NOT NULL, "
            + COLUMN_CINEMA_location + " TEXT NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CINEMA_cinemaId + " AUTOINCREMENT));";
    private static final String CREATE_WISHLIST_TABLE = "CREATE TABLE " + TABLENAME_WISHLIST + " ( "
            + COLUMN_WISHLIST_wishlistId + " INTEGER NOT NULL, "
            + COLUMN_WISHLIST_userId + " INTEGER, "
            + COLUMN_WISHLIST_movieId + " INTEGER, "
            + COLUMN_WISHLIST_hallId + " INTEGER, "
            + "PRIMARY KEY(" + COLUMN_WISHLIST_wishlistId + " AUTOINCREMENT), "
            + "FOREIGN KEY(" + COLUMN_WISHLIST_userId + ") REFERENCES " + TABLENAME_USER + "(" + COLUMN_USER_userID + "), "
            + "FOREIGN KEY(" + COLUMN_WISHLIST_movieId + ") REFERENCES " + TABLENAME_MOVIE + "(" + COLUMN_MOVIE_movieId + "), "
            + "FOREIGN KEY(" + COLUMN_WISHLIST_hallId + ") REFERENCES " + TABLENAME_HALL + "(" + COLUMN_HALL_hallId + "));";
    private static final String CREATE_CATEGORIE_TABLE = "CREATE TABLE " + TABLENAME_CATEGORIE + " ( "
            + COLUMN_CATEGORY_categoryId + " INTEGER NOT NULL, "
            + COLUMN_CATEGORY_name + " TEXT NOT NULL, "
            + COLUMN_CATEGORY_unicodeIcon + " TEXT, "
            + "PRIMARY KEY(" + COLUMN_CATEGORY_categoryId + " AUTOINCREMENT));";
    private static final String CREATE_CATEGORIESMOVIES_TABLE = "CREATE TABLE " + TABLENAME_CATEGORIE_MOVIE + " ( "
            + COLUMN_CATEGORIESMOVIES_categoryId + " INTEGER NOT NULL, "
            + COLUMN_CATEGORIESMOVIES_movieId + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CATEGORIESMOVIES_categoryId + ", " + COLUMN_CATEGORIESMOVIES_movieId + "), "
            + "FOREIGN KEY(" + COLUMN_CATEGORIESMOVIES_categoryId + ") REFERENCES " + TABLENAME_CATEGORIE + "(" + COLUMN_CATEGORY_categoryId + "), "
            + "FOREIGN KEY(" + COLUMN_CATEGORIESMOVIES_movieId + ") REFERENCES " + TABLENAME_MOVIE + "(" + COLUMN_MOVIE_movieId + "));";
    private static final String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLENAME_MOVIE + " ( "
            + COLUMN_MOVIE_movieId + " INTEGER NOT NULL, "
            + COLUMN_MOVIE_name + " TEXT NOT NULL,"
            + COLUMN_MOVIE_picture + " TEXT NOT NULL, "
            + COLUMN_MOVIE_video + " TEXT NOT NULL, "
            + COLUMN_MOVIE_duration + " NUMERIC NOT NULL, "
            + COLUMN_MOVIE_description + " TEXT NOT NULL, "
            + COLUMN_MOVIE_publishedDate + " INTEGER NOT NULL, " // STORING UNIX TIMESTAMP
            + "PRIMARY KEY(" + COLUMN_MOVIE_movieId + " AUTOINCREMENT));";
    private static final String CREATE_LOGGEDINUSER_TABLE = "CREATE TABLE " + TABLENAME_LOGGEDINUSER + " ( "
            + COLUMN_LOGGEDINUSER_userId + " INTEGER NOT NULL, "
            +  "PRIMARY KEY(" + COLUMN_LOGGEDINUSER_userId + "));";

    /**/
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // super(context, null, null, DATABASE_VERSION);
        // Passing no name => Database wont be saved as a file after closing. Good for testing, no need to upgrade db everytime.
        // TODO: Fix for final imlementation
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create DatabaseSchema
        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_CINEMA_TABLE);
        db.execSQL(CREATE_CATEGORIE_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CATEGORIESMOVIES_TABLE);
        db.execSQL(CREATE_HALL_TABLE);
        db.execSQL(CREATE_WISHLIST_TABLE);
        db.execSQL(CREATE_MOVIESCIENEMAS_TABLE);
        db.execSQL(CREATE_TICKET_TABLE);
        db.execSQL(CREATE_LOGGEDINUSER_TABLE);

        /* INSERT FAKE DATA*/
        db.execSQL(INSERT_MOVIE_TABLE);
        db.execSQL(INSERT_CINEMA_TABLE);
        db.execSQL(INSERT_CATEGORIES_TABLE);
        db.execSQL(INSERT_USER_TABLE);
        db.execSQL(INSERT_CATEGORIESMOVIES_TABLE);
        db.execSQL(INSERT_HALL_TABLE);
        db.execSQL(INSERT_WISHLIST_TABLE);
        db.execSQL(INSERT_MOVIESCINEMAS_TABLE);
        db.execSQL(INSERT_TICKET_TABLE);
        db.execSQL(INSERT_USERLOGGEDIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_CINEMA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_CATEGORIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_CATEGORIE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_HALL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_WISHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_MOVIES_CINEMAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_TICKET);
        onCreate(db);
    }
}
