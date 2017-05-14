package ng.volymat.popularmovies.custom;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ng.volymat.popularmovies.MainActivity;
import ng.volymat.popularmovies.Movie_Details;
import ng.volymat.popularmovies.R;
import ng.volymat.popularmovies.model.playing_now_item;
import ng.volymat.popularmovies.ui.DynamicHeightNetworkImageView;
import ng.volymat.popularmovies.ui.ImageLoaderHelper;


/**
 * Created by Nsikak  Thompson on 12/8/2016.
 */

public class Playing_Now_Adapter extends RecyclerView.Adapter<Playing_Now_Adapter.MyViewHolder> {

    private List<playing_now_item> moviesList;
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w300_and_h450_bestv2";
    private Cursor mCursor;
    private Context context;


    final private playingAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface playingAdapterOnClickHandler {
        void onClick(int  movie_id);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        DynamicHeightNetworkImageView thumbnail;
        TextView title,genre_ids, vote_average;

        public MyViewHolder(View view) {
            super(view);
           thumbnail = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.movie_name);
            genre_ids = (TextView) view.findViewById(R.id.genre_id);
            vote_average = (TextView)view.findViewById(R.id.vote_average);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int movie_id = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);
            mClickHandler.onClick(movie_id);
        }
    }


    public Playing_Now_Adapter( Context mContext, playingAdapterOnClickHandler clickHandler) {

        this.context = mContext;
        this.mClickHandler = clickHandler;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playing_now_item, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        mCursor.moveToPosition(position);

        String movie_title = mCursor.getString(MainActivity.INDEX_MOVIE_TITLE);
        String movie_genre = mCursor.getString(MainActivity.INDEX_MOVIE_GENRE);
        String thumbnail_url = mCursor.getString(MainActivity.INDEX_MOVIE_THUMBNAIL);
        String rating = String.valueOf(mCursor.getDouble(MainActivity.INDEX_MOVIE_RATING));
        int id = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);


        holder.itemView.setTag(id);
        holder.title.setText(movie_title);
        holder.genre_ids.setText(movie_genre);
        holder.vote_average.setText(rating);
             //poster_image

        holder.thumbnail.setImageUrl(IMAGE_BASE_URL + thumbnail_url, ImageLoaderHelper.getInstance(context).getImageLoader());
        holder.thumbnail.setAspectRatio((float) 1.10000);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }



}