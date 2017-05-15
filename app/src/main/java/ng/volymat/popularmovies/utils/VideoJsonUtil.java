package ng.volymat.popularmovies.utils;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ng.volymat.popularmovies.data.MovieContract;
import ng.volymat.popularmovies.model.MovieVideo;

/**
 * Created by Nsikak  Thompson on 5/15/2017.
 */

public class VideoJsonUtil {


    public static List<MovieVideo> getVideoFromJSON(JSONObject response) throws JSONException {

        List<MovieVideo> movieVideoList = new ArrayList<>();


        JSONArray feedArray = response.getJSONArray("results");


        for (int i = 0; i < feedArray.length(); i++) {
            JSONObject feedObj = (JSONObject) feedArray.get(i);

           MovieVideo movieVideo = new MovieVideo();

            movieVideo.setId(feedObj.getString("id"));
            movieVideo.setName(feedObj.getString("name"));
            movieVideo.setKey(feedObj.getString("key"));
            movieVideo.setSite(feedObj.getString("site"));
            movieVideo.setType(feedObj.getString("type"));
            movieVideoList.add(movieVideo);




        }




        return movieVideoList;
    }

}
