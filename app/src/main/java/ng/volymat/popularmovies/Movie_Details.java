package ng.volymat.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ng.volymat.popularmovies.custom.Playing_Now_Adapter;
import ng.volymat.volymat.R;
import ng.volymat.popularmovies.app.AppController;
import ng.volymat.popularmovies.ui.ImageLoaderHelper;

public class Movie_Details extends AppCompatActivity {

    private static final String TAG = Movie_Details.class.getSimpleName();
    private ImageView imagePoster;
    private int mMutedColor = 0xFF333333;
    private TextView movie_genres, overview;
    String movie_video_id ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout cab = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);



        Intent intent = getIntent();
        String poster_url = Playing_Now_Adapter.IMAGE_BASE_URL + intent.getStringExtra("backdrop_path");
        String movTitle = intent.getStringExtra("movie_title");
        String overiew_info = intent.getStringExtra("overview");

        ArrayList<Integer> genre_raw_ids = (ArrayList<Integer>) intent.getSerializableExtra("genre_id");
        Toast.makeText(this, poster_url +" "+ movTitle, Toast.LENGTH_SHORT).show();


        movie_genres = (TextView) findViewById(R.id.movie_genre);
        imagePoster = (ImageView) findViewById(R.id.photo);
        overview = (TextView) findViewById(R.id.overview_text);

        ImageLoaderHelper.getInstance(getApplicationContext()).getImageLoader()
                .get(poster_url, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                        Bitmap bitmap = imageContainer.getBitmap();
                        if (bitmap != null) {
                            Palette p = Palette.generate(bitmap, 16);
                            mMutedColor = p.getDarkMutedColor(0xFF333333);
                            imagePoster.setImageBitmap(imageContainer.getBitmap());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });

            cab.setTitle(movTitle);


        Playing_Now_Adapter.getMovieGenre(genre_raw_ids, movie_genres);
        overview.setText(overiew_info);

        String movie_id =  String.valueOf(getIntent().getIntExtra("id",0));
        Toast.makeText(this, movie_id, Toast.LENGTH_SHORT).show();
        String video_url = "https://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=" + Config.API_KEY + "&language=en-US";
        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
               video_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                VolleyLog.d(TAG, "Response: " + jsonObject.toString());
                if (jsonObject != null) {
                   movieVideoUrl(jsonObject);

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movie_video_id));
                startActivity(intent);

            }
        });
    }
    private void movieVideoUrl(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("results");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                movie_video_id = feedObj.getString("key");
                Toast.makeText(this, movie_video_id, Toast.LENGTH_SHORT).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
