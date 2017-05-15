package ng.volymat.popularmovies.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import javax.inject.Inject;

import ng.volymat.popularmovies.data.MovieContract;
import ng.volymat.popularmovies.model.Movie;

/**
 * Created by Nsikak  Thompson on 5/12/2017.
 */

public class FavoritesService {

    private final Context context;


    public FavoritesService(Context context) {
        this.context = context.getApplicationContext();
    }

    public void addToFavorites(Movie movie) {
       // context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movie.toContentValues());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.Favorites.COLUMN_MOVIE_ID_KEY, movie.getId());
        context.getContentResolver().insert(MovieContract.Favorites.CONTENT_URI, contentValues);
    }

    public void removeFromFavorites(Movie movie) {
        context.getContentResolver().delete(
                MovieContract.Favorites.CONTENT_URI,
                MovieContract.Favorites.COLUMN_MOVIE_ID_KEY + " = " + movie.getId(),
                null
        );
    }

    public boolean isFavorite(Movie movie) {
       boolean favorite = false;
        Cursor cursor = context.getContentResolver().query(
                MovieContract.Favorites.CONTENT_URI,
                null,
                MovieContract.Favorites.COLUMN_MOVIE_ID_KEY + " = " + movie.getId(),
                null,
                null
        );
        if (cursor != null) {
            favorite = cursor.getCount() != 0;
            cursor.close();

        }
        return favorite;
    }
}