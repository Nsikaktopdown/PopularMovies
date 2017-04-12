package ng.volymat.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

import ng.volymat.popularmovies.app.AppController;
import ng.volymat.volymat.R;
import ng.volymat.popularmovies.custom.Popular_Movie_Adapter;
import ng.volymat.popularmovies.model.popular_movie_item;

/**
 * Created by Nsikak  Thompson on 12/9/2016.
 */

public class Popular_Movie extends Fragment {
    private static final String TAG = Playing_Now.class.getSimpleName();
    private List<popular_movie_item> movieList = new ArrayList<>();
    private RecyclerView recyclerView2;
    private Popular_Movie_Adapter mAdapter;
    private static String MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?"+ Config.API_KEY+"&language=en-US&page=1";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        recyclerView2 = (RecyclerView) root.findViewById(R.id.movie_list1);
        mAdapter = new Popular_Movie_Adapter(movieList);
        recyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

       // recyclerView2.addItemDecoration(new DividerItemDecoration(getContext() , LinearLayoutManager.VERTICAL));
        recyclerView2.setHasFixedSize(true);
        // recyclerView2.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mProgressBar = (ProgressBar) root.findViewById(R.id.loading1);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout1);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        jsonReq();

        //setting recyclerview adapter
        recyclerView2.setAdapter(mAdapter);

        mProgressBar.setVisibility(View.GONE);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsonReq();
            }
        });

    }

    private void jsonReq(){
        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                MOVIE_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                VolleyLog.d(TAG, "Response: " + jsonObject.toString());
                if (jsonObject != null) {
                    movieParser(jsonObject);
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
    private void movieParser(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("results");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

               popular_movie_item item = new popular_movie_item();
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

}