package ng.volymat.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by Nsikak  Thompson on 5/9/2017.
 */

public class MovieContract {


    public static final String CONTENT_AUTORITY = "ng.volymat.popularmovies";

    public  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTORITY);

    public static final String PATH_MOVIE = "movie";


    public  static final   class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_GENRE = "genre";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_RATING= "rating";

        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

        public static final String COLUMN_OVERIEW = "overiew";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_BACKDROP_IMAGE_URL = "movie_title";




    }
}
