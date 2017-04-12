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
import android.widget.RelativeLayout;

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

import ng.volymat.popularmovies.custom.Playing_Now_Adapter;
import ng.volymat.volymat.R;
import ng.volymat.popularmovies.app.AppController;
import ng.volymat.popularmovies.model.playing_now_item;

/**
 * Created by Nsikak  Thompson on 12/8/2016.
 */

public class Playing_Now extends Fragment {
    private static final String TAG = Playing_Now.class.getSimpleName();
    private List<playing_now_item> movieList = new ArrayList<>();
    private RecyclerView recyclerView2;
    private Playing_Now_Adapter mAdapter;
    private static String MOVIE_URL = "https://api.themoviedb.org/3/movie/now_playing?" + Config.API_KEY+ "&language=en-US&page=1";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    int columnCount = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_playing_now, container, false);


        final RelativeLayout layer = (RelativeLayout) root.findViewById(R.id.layer1);

        recyclerView2 = (RecyclerView) root.findViewById(R.id.movie_list);
        mProgressBar = (ProgressBar) root.findViewById(R.id.loading);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);

        mAdapter = new Playing_Now_Adapter(movieList);


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        if(layoutManager.findFirstCompletelyVisibleItemPosition() ==0){
            layer.setVisibility(View.VISIBLE);
        }

        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setHasFixedSize(true);


        recyclerView2.setOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case  RecyclerView.SCROLL_STATE_IDLE:
                       layer.setVisibility(View.VISIBLE);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        layer.setVisibility(View.GONE);
                        break;
                }
            }
        });




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
                    mSwipeRefreshLayout.setRefreshing(false);
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

}
