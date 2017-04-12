package ng.volymat.popularmovies.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ng.volymat.popularmovies.ui.DynamicHeightNetworkImageView;
import ng.volymat.volymat.R;
import ng.volymat.popularmovies.model.popular_movie_item;
import ng.volymat.popularmovies.ui.ImageLoaderHelper;

/**
 * Created by Nsikak  Thompson on 12/9/2016.
 */

public class Popular_Movie_Adapter extends RecyclerView.Adapter<Popular_Movie_Adapter.MyViewHolder> {

    private List<popular_movie_item> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DynamicHeightNetworkImageView thumbnail;
        TextView title,genre_ids;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail1);
            title = (TextView) view.findViewById(R.id.movie_name1);
            genre_ids = (TextView) view.findViewById(R.id.genre_id1);
        }
    }


    public Popular_Movie_Adapter(List<popular_movie_item> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_movie_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
 popular_movie_item item = moviesList.get(position);

        //movie title
        holder.title.setText(item.getMovie_title());

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

        String genreStr = "";
        int genre_ids = 0;
        for (int str : item.getGenre()) {
            genre_ids += str  ;
            for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
                int key = entry.getKey();
                String value = entry.getValue();
                if (genre_ids == key) {
                    builder.append(value + ", ");
                    genreStr += builder;
                }
            }
        }

        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;

        holder.genre_ids.setText(genreStr);

        //poster_image
        Context context = holder.thumbnail.getContext();
        holder.thumbnail.setImageUrl("http://image.tmdb.org/t/p/w185/" + item.getPoster_path(), ImageLoaderHelper.getInstance(context).getImageLoader());
        holder.thumbnail.setAspectRatio((float) 1.49925);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}