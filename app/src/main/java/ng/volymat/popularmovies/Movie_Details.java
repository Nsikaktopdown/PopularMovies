package ng.volymat.popularmovies;

import android.content.Context;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ng.volymat.popularmovies.custom.MovieVideoAdapter;
import ng.volymat.popularmovies.custom.Playing_Now_Adapter;
import ng.volymat.popularmovies.R;
import ng.volymat.popularmovies.app.AppController;
import ng.volymat.popularmovies.data.MovieContract;
import ng.volymat.popularmovies.model.Movie;
import ng.volymat.popularmovies.model.MovieVideo;
import ng.volymat.popularmovies.sync.FavoritesService;
import ng.volymat.popularmovies.ui.DynamicHeightNetworkImageView;
import ng.volymat.popularmovies.ui.ImageLoaderHelper;
import ng.volymat.popularmovies.utils.NetworkUtils;

import static ng.volymat.popularmovies.R.drawable.holder;
import static ng.volymat.popularmovies.R.id.fab;
import static ng.volymat.popularmovies.custom.Playing_Now_Adapter.IMAGE_BASE_URL;
import static ng.volymat.popularmovies.sync.MovieSyncTask.getMoviecontentvalues;

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
    public static final int INDEX_ID = 6;
    public static  final int INDEX_MOVIE_ID = 7;




    private static final int ID_DETAIL_LOADER = 353;


    FavoritesService favoritesService;

    private Movie movie;
    private String sort_movie_id;

    private List<MovieVideo> videoList = new ArrayList<>();
    private RecyclerView recyclerView2;
    private MovieVideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        favoritesService = new FavoritesService(getApplicationContext());
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

        recyclerView2 = (RecyclerView) findViewById(R.id.video_list);

        movie_id = getIntent().getData();
        /* This connects our Activity into the loader lifecycle. */
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
        movie = new Movie();

        mAdapter = new MovieVideoAdapter(this, videoList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Movie_Details.this);
        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(mAdapter);


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


        int id = data.getInt(INDEX_ID);


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


        updateFab(movie);

       sort_movie_id = String.valueOf(data.getLong(INDEX_MOVIE_ID));
        Toast.makeText(this, sort_movie_id, Toast.LENGTH_SHORT).show();
        perfromVideoRequest(sort_movie_id);




    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onStart() {
        super.onStart();

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
    public  void perfromVideoRequest(String movie_id) {


        String url = NetworkUtils.getVideoRequestUrl(movie_id);
        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject jsonObject) {
                VolleyLog.d(TAG, "Response: " + jsonObject.toString());

                try {
                    getVideoFromJSON(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                VolleyLog.d(TAG, "Response: " + volleyError.toString());


            }
        });


        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);


    }

    public  void getVideoFromJSON(JSONObject response) throws JSONException {




        JSONArray feedArray = response.getJSONArray("results");


        for (int i = 0; i < feedArray.length(); i++) {
            JSONObject feedObj = (JSONObject) feedArray.get(i);

            MovieVideo movieVideo = new MovieVideo();

            movieVideo.setId(feedObj.getString("id"));
            movieVideo.setName(feedObj.getString("name"));
            movieVideo.setKey(feedObj.getString("key"));
            movieVideo.setSite(feedObj.getString("site"));
            movieVideo.setType(feedObj.getString("type"));
            videoList.add(movieVideo);




        }





    }

}
