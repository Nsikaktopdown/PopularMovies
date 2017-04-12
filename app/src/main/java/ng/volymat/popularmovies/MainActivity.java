package ng.volymat.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import ng.volymat.popularmovies.model.playing_now_item;
import ng.volymat.popularmovies.R;

import static ng.volymat.popularmovies.Config.API_KEY;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<playing_now_item> movieList = new ArrayList<>();
    private RecyclerView recyclerView2;
    private Playing_Now_Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    int columnCount = 2;
    String sortingOrder;

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


        mAdapter = new Playing_Now_Adapter(movieList);


        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(mAdapter);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sortingOrder = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_value));

        jsonReq(sortingOrder);

    }

    @Override
    protected void onResume() {
        super.onResume();
        jsonReq(sortingOrder);
    }

    @Override
    protected void onStart() {
        super.onStart();
        jsonReq(sortingOrder);

    }

    private void jsonReq(String sortOrder) {


        final String API_KEY_PARAMS = "api_key";
        Uri uri = Uri.parse(Config.BASE_URL).buildUpon()
                .appendEncodedPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAMS, Config.API_KEY)
                .build();

        String url = uri.toString();

        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                VolleyLog.d(TAG, "Response: " + jsonObject.toString());
                mProgressBar.setVisibility(View.INVISIBLE);
                if (jsonObject != null) {
                    movieParser(jsonObject);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                VolleyLog.d(TAG, "Response: " + volleyError.toString());
                Toast.makeText(MainActivity.this, "Couldn't get movies", Toast.LENGTH_SHORT).show();

            }
        });


        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);

    }

    private void movieParser(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("results");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                playing_now_item item = new playing_now_item();
                item.setId(feedObj.getInt("id"));
                item.setMovie_title(feedObj.getString("title"));


                item.setPoster_path(feedObj.getString("poster_path"));
                item.setOverview(feedObj.getString("overview"));
                item.setBackdrop(feedObj.getString("backdrop_path"));
                item.setVote_average(feedObj.getString("vote_average"));

                // Genre is json array
                JSONArray genreArry = feedObj.getJSONArray("genre_ids");
                ArrayList<Integer> genre = new ArrayList<Integer>();
                for (int j = 0; j < genreArry.length(); j++) {
                    genre.add((Integer) genreArry.get(j));
                }
                item.setGenre(genre);

                movieList.add(item);
            }

            // notify data changes to list adapater
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

}
