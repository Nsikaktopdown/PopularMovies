package ng.volymat.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import javax.inject.Inject;

import ng.volymat.popularmovies.custom.Playing_Now_Adapter;
import ng.volymat.popularmovies.R;
import ng.volymat.popularmovies.app.AppController;
import ng.volymat.popularmovies.data.MovieContract;
import ng.volymat.popularmovies.model.Movie;
import ng.volymat.popularmovies.sync.FavoritesService;
import ng.volymat.popularmovies.ui.DynamicHeightNetworkImageView;
import ng.volymat.popularmovies.ui.ImageLoaderHelper;

import static ng.volymat.popularmovies.R.drawable.holder;
import static ng.volymat.popularmovies.R.id.fab;
import static ng.volymat.popularmovies.custom.Playing_Now_Adapter.IMAGE_BASE_URL;

public class Movie_Details extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = Movie_Details.class.getSimpleName();
    private DynamicHeightNetworkImageView imagePoster;
    private int mMutedColor = 0xFF333333;
    private TextView movie_genres, overview, release_date_txt, rating_txt, movie_title;
    private Uri movie_id;
    private CollapsingToolbarLayout cab;
    private boolean isCollapse = true;
    private FloatingActionButton fab;


    public static final int INDEX_MOVIE_TITLE = 0;
    public static final int INDEX_MOVIE_RATING = 1;
    public static final int INDEX_MOVIE_BACKDROP = 2;
    public static final int INDEX_MOVIE_GENRE = 3;
    public static final int INDEX_MOVIE_DATE = 4;
    public static final int INDEX_MOVIE_OVERVIEW = 5;
    public static final int INDEX_MOVIE_ID = 6;


    private static final int ID_DETAIL_LOADER = 353;

    @Inject
    FavoritesService favoritesService;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cab = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        cab.setTitleEnabled(false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        movie_genres = (TextView) findViewById(R.id.movie_genre);
        imagePoster = (DynamicHeightNetworkImageView) findViewById(R.id.photo);
        overview = (TextView) findViewById(R.id.overview_text);
        release_date_txt = (TextView) findViewById(R.id.date_text);
        rating_txt = (TextView) findViewById(R.id.rating_text);
        movie_title = (TextView) findViewById(R.id.detail_movie_title);

        movie_id = getIntent().getData();
        /* This connects our Activity into the loader lifecycle. */
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        movie = new Movie();
    }

    private void updateFab(Movie movie) {
        if(movie == null){

            Toast.makeText(this, "model null", Toast.LENGTH_SHORT).show();
        }

        else{
            if (favoritesService.isFavorite(movie)) {
                fab.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        movie_id,
                        MovieContract.MOVIE_DETAIL_ROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }


        int id = data.getInt(INDEX_MOVIE_ID);


        String backdrop_url = data.getString(INDEX_MOVIE_BACKDROP);
        movie.setBackdrop_url(backdrop_url);
        imagePoster.setImageUrl(IMAGE_BASE_URL + backdrop_url, ImageLoaderHelper.getInstance(this).getImageLoader());
        imagePoster.setAspectRatio((float) 1.10000);

        /*Read overviews from Db*/
        String overview_data = data.getString(INDEX_MOVIE_OVERVIEW);
        /* Set Text*/
        overview.setText(overview_data);


        String date = data.getString(INDEX_MOVIE_DATE);
        release_date_txt.setText(date);

        String title = data.getString(INDEX_MOVIE_TITLE);
        title = "* " + title + " *";
        movie_title.setText(title);


        String genre = data.getString(INDEX_MOVIE_GENRE);
        movie_genres.setText(genre);

        long rating = data.getLong(INDEX_MOVIE_RATING);
        rating_txt.setText(String.valueOf(rating + " *"));

        movie.setId(id);
        movie.setDate(date);
        movie.setTitle(title);
        movie.setOverviews(overview_data);
        movie.setGenre(genre);
        movie.setRating(rating);



       // updateFab(movie);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.fab:
                if (favoritesService.isFavorite(movie)) {
                    favoritesService.removeFromFavorites(movie);
                    showSnackbar("removed from favourite");
                } else {
                    favoritesService.addToFavorites(movie);
                    showSnackbar("Added as Favourite");
                }
                updateFab(movie);

                break;
        }

    }

    private void showSnackbar(String message) {
        Snackbar.make(cab, message, Snackbar.LENGTH_LONG).show();
    }

}
