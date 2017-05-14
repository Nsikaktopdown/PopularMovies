package ng.volymat.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import ng.volymat.popularmovies.Config;
import ng.volymat.popularmovies.MainActivity;
import ng.volymat.popularmovies.app.AppController;
import ng.volymat.popularmovies.data.MoviePreferences;
import ng.volymat.popularmovies.sync.MovieSyncTask;

import static ng.volymat.popularmovies.app.AppController.TAG;

/**
 * Created by Nsikak  Thompson on 5/10/2017.
 */

public class NetworkUtils {


    public static String getUrl(Context context){

      String sortOrderpref=   MoviePreferences.sortOrder(context);
        return buildRequestUrl(sortOrderpref);
    }






    private static String buildRequestUrl(String sortOrder) {
        final String API_KEY_PARAMS = "api_key";
        Uri uri = Uri.parse(Config.BASE_URL).buildUpon()
                .appendEncodedPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAMS, Config.API_KEY)
                .build();

        String url = uri.toString();
        return url;
    }
}
