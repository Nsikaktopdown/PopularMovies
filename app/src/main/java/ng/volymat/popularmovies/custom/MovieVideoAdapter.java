package ng.volymat.popularmovies.custom;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ng.volymat.popularmovies.MainActivity;
import ng.volymat.popularmovies.R;
import ng.volymat.popularmovies.model.MovieVideo;
import ng.volymat.popularmovies.model.playing_now_item;
import ng.volymat.popularmovies.ui.DynamicHeightNetworkImageView;
import ng.volymat.popularmovies.ui.ImageLoaderHelper;

import static ng.volymat.popularmovies.R.dimen.movie_title;

/**
 * Created by Nsikak  Thompson on 5/15/2017.
 */

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MyViewHolder> {

    private List<MovieVideo> videosList;
    public static final String IMAGE_BASE_URL = "http://img.youtube.com/vi/";

    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder  {
        DynamicHeightNetworkImageView video_thumbnail;
        TextView video_title, video_site, video_type;

        public MyViewHolder(View view) {
            super(view);
            video_thumbnail = (DynamicHeightNetworkImageView) view.findViewById(R.id.video_thumbnail);
            video_title = (TextView) view.findViewById(R.id.video_title);
            video_type = (TextView) view.findViewById(R.id.video_type);
            video_site = (TextView) view.findViewById(R.id.video_site);

        }



    }


    public MovieVideoAdapter(Context mContext, List<MovieVideo> movieVideoList) {

        this.context = mContext;
        this.videosList = movieVideoList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MovieVideo movieVideo = videosList.get(position);

        holder.video_title.setText(movieVideo.getName());
        holder.video_site.setText("Watch on " + movieVideo.getSite());
        holder.video_type.setText(movieVideo.getType());

        String thumbnail_url = movieVideo.getKey() + "/0.jpg";

        holder.video_thumbnail.setImageUrl(IMAGE_BASE_URL + thumbnail_url, ImageLoaderHelper.getInstance(context).getImageLoader());
        holder.video_thumbnail.setAspectRatio((float) 1.10000);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(movieVideo.isYoutubeVideo()){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + movieVideo.getKey()));
                    context.startActivity(intent);
                }
                }

        });

    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }


}