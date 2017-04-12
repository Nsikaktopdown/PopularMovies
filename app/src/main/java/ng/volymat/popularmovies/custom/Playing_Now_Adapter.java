package ng.volymat.popularmovies.custom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ng.volymat.popularmovies.Movie_Details;
import ng.volymat.volymat.R;
import ng.volymat.popularmovies.model.playing_now_item;
import ng.volymat.popularmovies.ui.DynamicHeightNetworkImageView;
import ng.volymat.popularmovies.ui.ImageLoaderHelper;


/**
 * Created by Nsikak  Thompson on 12/8/2016.
 */

public class Playing_Now_Adapter extends RecyclerView.Adapter<Playing_Now_Adapter.MyViewHolder> {

    private List<playing_now_item> moviesList;
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w300_and_h450_bestv2";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DynamicHeightNetworkImageView thumbnail;
        TextView title,genre_ids, vote_average;

        public MyViewHolder(View view) {
            super(view);
           thumbnail = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.movie_name);
            genre_ids = (TextView) view.findViewById(R.id.genre_id);
            vote_average = (TextView)view.findViewById(R.id.vote_average);
        }
    }


    public Playing_Now_Adapter(List<playing_now_item> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playing_now_item, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        playing_now_item item = moviesList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context con = holder.itemView.getContext();
                playing_now_item item = moviesList.get(position);
                Intent intent = new Intent(con, Movie_Details.class);
                intent.putExtra("backdrop_path",item.getBackdrop());
                intent.putExtra("movie_title", item.getMovie_title());
                intent.putExtra("genre_id", item.getGenre());
                intent.putExtra("overview", item.getOverview());
                intent.putExtra("id", item.getId());
                con.startActivity(intent);
            }
        });
            //movie title
        holder.title.setText(item.getMovie_title());

            // genre_ids
        getMovieGenre(item.getGenre(),holder.genre_ids);

             //poster_image
        Context context = holder.thumbnail.getContext();
        holder.thumbnail.setImageUrl(IMAGE_BASE_URL + item.getPoster_path(), ImageLoaderHelper.getInstance(context).getImageLoader());
        holder.thumbnail.setAspectRatio((float) 1.10000);

        //vote_average
        holder.vote_average.setText(item.getVote_average());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static void getMovieGenre( ArrayList<Integer> genre_array, TextView genreTextView){
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
        genreTextView.setText(genreString);
    }
}