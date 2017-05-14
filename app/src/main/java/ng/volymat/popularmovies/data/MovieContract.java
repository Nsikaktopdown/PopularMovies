package ng.volymat.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

import static android.os.Build.VERSION_CODES.M;
import static ng.volymat.popularmovies.data.MovieContract.MovieEntry.CONTENT_URI;

/**
 * Created by Nsikak  Thompson on 5/9/2017.
 */

public class MovieContract {


    public static final String CONTENT_AUTHORITY = "ng.volymat.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAVORITES = "favorites";

    public static final String COLUMN_MOVIE_KEY = "movie_key";


    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";


        public static final String _ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_GENRE = "genre";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_BACKDROP_IMAGE_URL = "backdrop_url";


    }


    /*
    * The columns of data that we are interested in displaying within our MainActivity's list of
    * movie data.
    */
    public static final String[] MAIN_MOVIE_ROJECTION = {
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_RATING,
            MovieEntry.COLUMN_THUMBNAIL_URL,
            MovieEntry.COLUMN_GENRE,
            MovieEntry._ID
    };


    public static final String[] MOVIE_DETAIL_ROJECTION = {
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_RATING,
            MovieEntry.COLUMN_BACKDROP_IMAGE_URL,
            MovieEntry.COLUMN_GENRE,
            MovieEntry.COLUMN_DATE,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry._ID
    };
    public static String getMovieIDFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }


    public static Uri buildMovieUriWithID(int id) {
        return CONTENT_URI.buildUpon()
                .appendPath(Long.toString(id))
                .build();
    }

    public static long getIdFromUri(Uri uri) {
        return ContentUris.parseId(uri);
    }

    public static final class Favorites implements BaseColumns {
        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE
                        + "/" + PATH_FAVORITES;

        public static final String TABLE_NAME = "favorites";
        public static  final String _ID = "id";
        public  static  final  String COLUMN_MOVIE_ID_KEY = "movie_key";





        private static final String[] COLUMNS_MOVIE_FAVORITE = {
                _ID,
                COLUMN_MOVIE_ID_KEY};

        private Favorites() {
        }


    }
}
