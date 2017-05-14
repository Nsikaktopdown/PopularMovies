package ng.volymat.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static ng.volymat.popularmovies.data.MovieContract.Favorites.COLUMN_MOVIE_ID_KEY;

/**
 * Created by Nsikak  Thompson on 5/9/2017.
 */

public class MovieDdHelper extends SQLiteOpenHelper {


    public static final String MOVIE_HELPER_TAG = MovieDdHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 2;

    public MovieDdHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
           * This String will contain a simple SQL statement that will create a table that will
           * cache our weather data.
           */
    public final String SQL_CREATE_MOVIE_TABLE =

            "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                    MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                    MovieContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_GENRE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_BACKDROP_IMAGE_URL + " TEXT NOT NULL ," +


                /*
                 * To ensure this table can only contain one weather entry per date, we declare
                 * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a weather entry for a certain date and we attempt to
                 * insert another weather entry with that date, we replace the old weather entry.
                 */
                    " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


    public static final String SQL_FAVORITE_TABLE =
            "CREATE TABLE " + MovieContract.Favorites.TABLE_NAME + " (" +
                    MovieContract.Favorites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +

                    " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                    MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") " +

                    " );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_FAVORITE_TABLE);

        Log.v(MOVIE_HELPER_TAG, "Database created Successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + MovieContract.Favorites.TABLE_NAME);
        onCreate(db);
    }
}
