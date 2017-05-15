package ng.volymat.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import ng.volymat.popularmovies.app.AppController;
import ng.volymat.popularmovies.data.MovieContract;
import ng.volymat.popularmovies.utils.MovieDBJsonUtils;
import ng.volymat.popularmovies.utils.NetworkUtils;

import static android.R.attr.id;
import static ng.volymat.popularmovies.app.AppController.TAG;
/**
 * Created by Nsikak  Thompson on 5/10/2017.
 */

public class MovieSyncTask {


    synchronized public static void synMovie(Context context) {

        try {
            /*get the request URL*/
            String requesturl = NetworkUtils.getUrl(context);

            perfromRequest(context,requesturl);


        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

    public static void perfromRequest(final Context context, String url) {


        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject jsonObject) {
                VolleyLog.d(TAG, "Response: " + jsonObject.toString());

                try {
                    getMoviecontentvalues(context, jsonObject);
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




        public static void getMoviecontentvalues(Context context, JSONObject jsonObject) throws JSONException{

            ContentValues[] movieValues = MovieDBJsonUtils.getMovieFromJSON(jsonObject);

            if (movieValues != null && movieValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver moviesContentResolver = context.getContentResolver();

//              COMPLETED (4) If we have valid results, delete the old data and insert the new
                /* Delete old weather data because we don't need to keep multiple days' data */
                 moviesContentResolver.delete(
                         MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new weather data into Sunshine's ContentProvider */
              int id = moviesContentResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues);

                ContentValues values = new ContentValues();
                values.put(MovieContract.Favorites.COLUMN_MOVIE_ID_KEY, id);

            }
        }
}



