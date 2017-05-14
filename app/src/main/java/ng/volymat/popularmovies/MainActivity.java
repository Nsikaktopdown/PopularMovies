package ng.volymat.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ng.volymat.popularmovies.app.AppController;
import ng.volymat.popularmovies.custom.Playing_Now_Adapter;
import ng.volymat.popularmovies.data.MovieContract;
import ng.volymat.popularmovies.model.playing_now_item;
import ng.volymat.popularmovies.R;
import ng.volymat.popularmovies.sync.MovieSynIntentUtil;

import static android.R.attr.data;
import static ng.volymat.popularmovies.Config.API_KEY;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, Playing_Now_Adapter.playingAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView2;
    private Playing_Now_Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mProgressBar;
    String sortingOrder;


    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_RATING = 2;
    public static final int INDEX_MOVIE_THUMBNAIL = 3;
    public static final int INDEX_MOVIE_GENRE = 4;


    public static final int ID_MOVIE_LOADER = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView2 = (RecyclerView) findViewById(R.id.movie_list);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);


        mAdapter = new Playing_Now_Adapter(this, this);


        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(mAdapter);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sortingOrder = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_value));

       /* intialized the loader*/
        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

        //      COMPLETED (13) Call MovieSyncUtils's startImmediateSync method
        MovieSynIntentUtil.startImmediateSync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the MainActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_MOVIE_LOADER:
                /* URI for all rows of movie data in our movie table */
                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;

                return new CursorLoader(this,
                        movieQueryUri,
                        MovieContract.MAIN_MOVIE_ROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        recyclerView2.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showWeatherDataView();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);

    }

    private void showWeatherDataView() {
        /* First, hide the loading indicator */
        mProgressBar.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        recyclerView2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int movie_id) {
        Intent movieDetailIntent = new Intent(MainActivity.this, Movie_Details.class);
        Uri id_uri = MovieContract.buildMovieUriWithID(movie_id);
        movieDetailIntent.setData(id_uri);
        startActivity(movieDetailIntent);
    }
}
