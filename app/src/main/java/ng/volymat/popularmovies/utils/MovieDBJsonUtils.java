package ng.volymat.popularmovies.utils;

import android.content.ContentValues;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ng.volymat.popularmovies.data.MovieContract;
import ng.volymat.popularmovies.model.playing_now_item;

/**
 * Created by Nsikak  Thompson on 5/10/2017.
 */

public class MovieDBJsonUtils {


    public static ContentValues[]  getMovieFromJSON(JSONObject response) throws  JSONException {



            JSONArray feedArray = response.getJSONArray("results");

            ContentValues[] moviecontentvalues = new ContentValues[feedArray.length()];

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                ContentValues moviesvalues = new ContentValues();


                moviesvalues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,feedObj.getInt("id"));
                moviesvalues.put(MovieContract.MovieEntry.COLUMN_TITLE,feedObj.getString("title"));
                moviesvalues.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL_URL,feedObj.getString("poster_path"));
                moviesvalues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,feedObj.getString("overview"));
                moviesvalues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_IMAGE_URL,feedObj.getString("backdrop_path"));
                moviesvalues.put(MovieContract.MovieEntry.COLUMN_RATING,feedObj.getString("vote_average"));
                moviesvalues.put(MovieContract.MovieEntry.COLUMN_DATE,feedObj.getString("release_date"));

                // Genre is json array
                JSONArray genreArry = feedObj.getJSONArray("genre_ids");
                ArrayList<Integer> genre = new ArrayList<Integer>();
                for (int j = 0; j < genreArry.length(); j++) {
                    genre.add((Integer) genreArry.get(j));
                }
                String genreString = getMovieGenre(genre);
                moviesvalues.put(MovieContract.MovieEntry.COLUMN_GENRE, genreString);

                moviecontentvalues[i] = moviesvalues;


            }




        return moviecontentvalues;
    }


    private static String getMovieGenre( ArrayList<Integer> genre_array){
        // genre_ids
        StringBuilder builder = new StringBuilder();

        Map<Integer, String> genreMap = new HashMap<Integer, String>() {
            {
                put(28, "Action");
                put(12, "Adventure");
                put(16, "Animation");
                put(35, "Comedy");
                put(80, "Crime");
                put(99, "Documentary");
                put(18, "Drama");
                put(10751, "Family");
                put(14, "Fantasy");
                put(10769, "Foreign");
                put(36, "History");
                put(27, "Horror");
                put(10402, "Music");
                put(9648, "Mystery");
                put(10749, "Romance");
                put(878, "Science Fiction");
                put(10770, "TV Movie");
                put(53, "Thriller");
                put(10752, "War");
                put(37, "Western");
            }
        };

        String genreString= "";
        int genre_int = 0;
        for (int str : genre_array) {
            genre_int += str  ;
            for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
                int key = entry.getKey();
                String value = entry.getValue();
                if (genre_int == key) {
                    builder.append(value + ", ");
                    genreString += builder;


                }
            }
        }
        genreString = genreString.length() > 0 ? genreString.substring(0,
                genreString.length() - 2) : genreString;
       return genreString;
    }
}
