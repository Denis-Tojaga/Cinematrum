package com.mob3000.cinematrum.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


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
    public static final String COLUMN_CINEMA_latitude = "latitude";
    public static final String COLUMN_CINEMA_longitude = "longitude";
    // COLUMNS - WHISLIST
    public static final String COLUMN_WISHLIST_wishlistId = "wishlist_id";
    public static final String COLUMN_WISHLIST_userId = "user_id";
    public static final String COLUMN_WISHLIST_movieId = "movie_id";
    public static final String COLUMN_WISHLIST_hallId = "hall_id"; // TODO Why so we save the hall ID for a wishilist entry?
    // COLUMNS - CATEGORY
    public static final String COLUMN_CATEGORY_categoryId = "category_id";
    public static final String COLUMN_CATEGORY_name = "name";
    public static final String COLUMN_CATEGORY_unicodeIcon = "unicodeIcon";
    public static final String COLUMN_CATEGORY_imageUrl = "imageUrl";
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
    public static final String COLUMN_MOVIE_rating = "rating";
    public static final String COLUMN_MOVIE_movieTrailerURL = "movieTrailerURL";

    // COLUMNS - LOGGEDINUSER
    public static final String COLUMN_LOGGEDINUSER_userId = "user_id";
    // INSERT DATA

    //Denis 7.10.2021
    public static final String INSERT_MOVIE_TABLE = "INSERT INTO " + TABLENAME_MOVIE + " (" + COLUMN_MOVIE_name + ", " + COLUMN_MOVIE_picture + ", " + COLUMN_MOVIE_video + ", " + COLUMN_MOVIE_duration + ", " + COLUMN_MOVIE_description + ", " + COLUMN_MOVIE_publishedDate + ", " + COLUMN_MOVIE_rating + ", " + COLUMN_MOVIE_movieTrailerURL + ") "
            + "VALUES (\"No Time to Die\",\"https://static.posters.cz/image/750/plakater/james-bond-no-time-to-die-azure-teaser-i109817.jpg\", \"\", \"163\", \"James Bond has left active service. His peace is short-lived when Felix Leiter, an old friend from the CIA, turns up asking for help, leading Bond onto the trail of a mysterious villain armed with dangerous new technology.\", 1631451990, \"7.5/10\", \"https://www.youtube.com/watch?v=N_gD9-Oa0fg&t=2s\\\"),"
            + "(\"IT Ends\",\"https://m.media-amazon.com/images/M/MV5BYTJlNjlkZTktNjEwOS00NzI5LTlkNDAtZmEwZDFmYmM2MjU2XkEyXkFqcGdeQXVyNjg2NjQwMDQ@._V1_.jpg\", \"\", 169, \"Twenty-seven years after their first encounter with the terrifying Pennywise, the Losers Club have grown up and moved away, until a devastating phone call brings them back.\",1631451990, \"6.5/10\", \"https://www.youtube.com/watch?v=xhJ5P7Up3jA\" ),"
            + "(\"The Shawshank Redemption\",\"https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg\", \"\", 142, \"Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.\",757382400, \"9.3/10\", \"https://www.youtube.com/watch?v=NmzuHjWmXOc\" ),"
            + "(\"The Godfather\",\"https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg\", \"\", 175, \"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",63072000, \"9.2/10\", \"https://www.youtube.com/watch?v=sY1S34973zA\" ),"
            + "(\"The Dark Knight\",\"https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg\", \"\", 152, \"When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.\",1199145600, \"9.0/10\", \"https://www.youtube.com/watch?v=EXeTwQWrcwY\" ),"
            + "(\"Pulp Fiction\",\"https://i-viaplay-com.akamaized.net/viaplay-prod/771/672/1473257890-66ec43721fe0fd0073af100473a09da74924816c.jpg?width=400&height=600\", \"\", 154, \"The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.\",757382400, \"8.9/10\", \"https://www.youtube.com/watch?v=s7EdQ4FqbhY\" ),"
            + "(\"Fight Club\",\"https://www.arthipo.com/image/cache/catalog/poster/movie/1-758/pfilm135-fight-club-dovus-kulubu-hd-poster-satisi-brad-pitt-1000x1000.jpg\", \"\", 139, \"An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.\",915148800, \"8.8/10\", \"https://www.youtube.com/watch?v=qtRKdVHc-cE\" ),"
            + "(\"Se7en\",\"https://m.media-amazon.com/images/M/MV5BOTUwODM5MTctZjczMi00OTk4LTg3NWUtNmVhMTAzNTNjYjcyXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg\", \"\", 127, \"Two detectives, a rookie and a veteran, hunt a serial killer who uses the seven deadly sins as his motives.\",788918400, \"8.6/10\", \"https://www.youtube.com/watch?v=znmZoVkCjpI\" ),"
            + "(\"Parasite\",\"https://m.media-amazon.com/images/I/91sustfojBL._AC_SL1500_.jpg\", \"\", 127, \"Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.\",1546300800, \"8.6/10\", \"https://www.youtube.com/watch?v=5xH0HfJHsaY\" ),"
            + "(\"It Happened One Night\",\"https://m.media-amazon.com/images/M/MV5BYzJmMWE5NjAtNWMyZS00NmFiLWIwMDgtZDE2NzczYWFhNzIzXkEyXkFqcGdeQXVyNjc1NTYyMjg@._V1_.jpg\", \"\", 105, \"A renegade reporter trailing a young runaway heiress for a big story joins her on a bus heading from Florida to New York, and they end up stuck with each other when the bus leaves them behind at one of the stops.\",-1136073600, \"8.1/10\", \"https://www.youtube.com/watch?v=Kd509cLN-9U\" ),"
            + "(\"Casablanca\",\"https://m.media-amazon.com/images/I/61-UNGpgYLL._AC_SY679_.jpg\", \"\", 102, \"A cynical expatriate American cafe owner struggles to decide whether or not to help his former lover and her fugitive husband escape the Nazis in French Morocco.\",-883612800, \"8.5/10\", \"https://www.youtube.com/watch?v=BkL9l7qovsE\" ),"
            + "(\"The Shape of Water\",\"https://m.media-amazon.com/images/M/MV5BNGNiNWQ5M2MtNGI0OC00MDA2LWI5NzEtMmZiYjVjMDEyOWYzXkEyXkFqcGdeQXVyMjM4NTM5NDY@._V1_.jpg\", \"\", 123, \"At a top secret research facility in the 1960s, a lonely janitor forms a unique relationship with an amphibious creature that is being held in captivity.\",1483228800, \"7.3/10\", \"https://www.youtube.com/watch?v=XFYWazblaUA\" ),"
            + "(\"The Fault in Our Stars\",\"https://m.media-amazon.com/images/I/81pw2SdujuL._SY800_.jpg\", \"\", 126, \"Two teenage cancer patients begin a life-affirming journey to visit a reclusive author in Amsterdam.\",1388534400, \"7.7/10\", \"https://www.youtube.com/watch?v=9ItBvH5J6ss\" ),"
            + "(\"The Notebook\",\"https://m.media-amazon.com/images/M/MV5BMTk3OTM5Njg5M15BMl5BanBnXkFtZTYwMzA0ODI3._V1_FMjpg_UX1000_.jpg\", \"\", 123, \"A poor yet passionate young man falls in love with a rich young woman, giving her a sense of freedom, but they are soon separated because of their social differences.\",1072915200, \"7.8/10\", \"https://www.youtube.com/watch?v=yDJIcYE32NU\" ),"
            + "(\"Iron Man\",\"https://m.media-amazon.com/images/M/MV5BMTczNTI2ODUwOF5BMl5BanBnXkFtZTcwMTU0NTIzMw@@._V1_.jpg\", \"\", 126, \"After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.\",1199145600, \"7.9/10\", \"https://www.youtube.com/watch?v=8ugaeA-nMTc\" ),"
            + "(\"Thor\",\"https://m.media-amazon.com/images/M/MV5BOGE4NzU1YTAtNzA3Mi00ZTA2LTg2YmYtMDJmMThiMjlkYjg2XkEyXkFqcGdeQXVyNTgzMDMzMTg@._V1_.jpg\", \"\", 115, \"The powerful but arrogant god Thor is cast out of Asgard to live amongst humans in Midgard (Earth), where he soon becomes one of their finest defenders.\",1293840000, \"7.0/10\", \"https://www.youtube.com/watch?v=9ItBvH5J6ss\" ),"
            + "(\"Captain America: The First Avenger\",\"https://m.media-amazon.com/images/M/MV5BMTYzOTc2NzU3N15BMl5BanBnXkFtZTcwNjY3MDE3NQ@@._V1_.jpg\", \"\", 125, \"Steve Rogers, a rejected military soldier, transforms into Captain America after taking a dose of a Super-Soldier serum. But being Captain America comes at a price as he attempts to take down a war monger and a terrorist organization.\",1293840000, \"6.9/10\", \"https://www.youtube.com/watch?v=JerVrbLldXw\" ),"
            + "(\"Avengers: Infinity War\",\"https://m.media-amazon.com/images/I/A1t8xCe9jwL._AC_SL1500_.jpg\", \"\", 149, \"The Avengers and their allies must be willing to sacrifice all in an attempt to defeat the powerful Thanos before his blitz of devastation and ruin puts an end to the universe.\",1514764800, \"8.4/10\", \"https://www.youtube.com/watch?v=6ZfuNTqbHE8\" ),"
            + "(\"Avengers: Endgame\",\"https://m.media-amazon.com/images/I/81ai6zx6eXL._AC_SL1304_.jpg\", \"\", 181, \"After the devastating events of Avengers: Infinity War (2018), the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.\",1546300800, \"8.4/10\", \"https://www.youtube.com/watch?v=TcMBFSGVi1c\" )";

    public static final String INSERT_USER_TABLE = "INSERT INTO " + TABLENAME_USER + " (" + COLUMN_USER_username + ", " + COLUMN_USER_email + ", " + COLUMN_USER_password + ", " + COLUMN_USER_salt + ", " + COLUMN_USER_userType + ", " + COLUMN_USER_telephone + ") "
            + "VALUES (\"user1\",\"email1\",\"password1\",\"salt1\",01234, \"admin\"),"
            + "(\"user3\",\"email2\",\"password3\",\"salt2\",012348790809, \"user\"),"
            + "(\"user2\",\"email3\",\"password2\",\"salt3\",012348790809, \"user\");";
    public static final String INSERT_TICKET_TABLE = "INSERT INTO " + TABLENAME_TICKET + " (" + COLUMN_TICKET_userID + ", " + COLUMN_TICKET_moviesCinemaID + ", " + COLUMN_TICKET_reservedAt + ", " + COLUMN_TICKET_rowNumber + ", " + COLUMN_TICKET_seatNumber + ") "
            + "VALUES (1,1,1631451997,1,2),(1,1,1631451997,1,3);";
    public static final String INSERT_CINEMA_TABLE = "INSERT INTO " + TABLENAME_CINEMA + " (" + COLUMN_CINEMA_name + ", " + COLUMN_CINEMA_latitude + ", " + COLUMN_CINEMA_longitude  + ") "
            + "VALUES (\"Hönefoss Cinema\", 60.167459331736254, 10.256550735053501), (\"Ciname Oslo\", 59.914057645043584, 10.733265608135563), (\"Oslo Theater\", 59.87228782613781, 10.808862765353563);";
    public static final String INSERT_HALL_TABLE = "INSERT INTO " + TABLENAME_HALL + " (" + COLUMN_HALL_cinemaId + ", " + COLUMN_HALL_rows + ", " + COLUMN_HALL_seatsPerRow + ") "
            + "VALUES (1,10,7), (2,8,20);";
    public static final String INSERT_WISHLIST_TABLE = "INSERT INTO " + TABLENAME_WISHLIST + " ( " + COLUMN_WISHLIST_userId + ", " + COLUMN_WISHLIST_movieId + ", " + COLUMN_WISHLIST_hallId + ") "
            + "VALUES (1, 1,1), (1,2,1); ";
    public static final String INSERT_MOVIESCINEMAS_TABLE = "INSERT INTO " + TABLENAME_MOVIES_CINEMAS + " (" + COLUMN_MOVIESCINEMAS_movieID + ", " + COLUMN_MOVIESCINEMAS_hallId + ", " + COLUMN_MOVIESCINEMAS_price + ", " + COLUMN_MOVIESCINEMAS_seatsAvailable + ", " + COLUMN_MOVIESCINEMAS_allSeats + ", " + COLUMN_MOVIESCINEMAS_date + ") "
            + "VALUES (1,1,13.99,20,100,1671451990),"
            + "(1,1,14.99,20,100,1638724207), "
            + "(2,1,20.99,20,100,1731451990),"
            + "(1,2,10.99,20,100,1691451990);";
    public static final String INSERT_CATEGORIES_TABLE = "INSERT INTO " + TABLENAME_CATEGORIE + " (" + COLUMN_CATEGORY_name + ", " + COLUMN_CATEGORY_imageUrl + ") "
            + "VALUES (\"Comedy\", \"https://i.pinimg.com/originals/ef/f8/b9/eff8b9e41133bd9b2b8b733c56b2cbea.jpg\"),(\"Drama\", \"https://miro.medium.com/max/1000/1*T-544XBLkxSr4y_aAo5OfQ.jpeg\"),(\"Horror\", \"https://i.insider.com/5e5036b5a9f40c18895e8d88?width=700\"),(\"Action\", \"https://i.insider.com/5b58c81ddce2e936008b4588?width=700\"),(\"Super Hero\",\"https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/spider-man-movies-in-order-index-1631199371.jpeg?crop=0.565xw:1.00xh;0.435xw,0&resize=640:*\"),(\"Romance\", \"https://thoughtcatalog.com/wp-content/uploads/2013/09/istock_000015777770medium2.jpg\");";
    public static final String INSERT_CATEGORIESMOVIES_TABLE = "INSERT INTO " + TABLENAME_CATEGORIE_MOVIE + " (" + COLUMN_CATEGORIESMOVIES_categoryId + ", " + COLUMN_CATEGORIESMOVIES_movieId + ") "
            + "VALUES (2,1),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),(3,2),(4,5),(4,7),(4,15),(4,16),(4,17),(4,18),(4,19),(5,15),(5,16),(5,17),(5,18),(5,19),(5,5),(6,10),(6,11),(6,12),(6,13);";
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
            + COLUMN_CINEMA_latitude + " NUMERIC NOT NULL, "
            + COLUMN_CINEMA_longitude + " NUMERIC NOT NULL, "
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
            + COLUMN_CATEGORY_imageUrl + " TEXT, "
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
            + COLUMN_MOVIE_rating + " TEXT, "
            + COLUMN_MOVIE_movieTrailerURL + " TEXT, "
            + "PRIMARY KEY(" + COLUMN_MOVIE_movieId + " AUTOINCREMENT));";
    private static final String CREATE_LOGGEDINUSER_TABLE = "CREATE TABLE " + TABLENAME_LOGGEDINUSER + " ( "
            + COLUMN_LOGGEDINUSER_userId + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_LOGGEDINUSER_userId + "));";

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
