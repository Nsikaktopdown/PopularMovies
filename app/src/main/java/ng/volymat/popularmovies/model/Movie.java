package ng.volymat.popularmovies.model;

import android.content.ContentValues;

import ng.volymat.popularmovies.data.MovieContract;

import static ng.volymat.popularmovies.R.string.overview;

/**
 * Created by Nsikak  Thompson on 5/12/2017.
 */

public class Movie {

    private String title;
    private  int id;
    private  String overviews;
    private  String thumnail_url;
    private String date;
    private String genre;
    private Long rating;
    private String backdrop_url;
    private long movie_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverviews() {
        return overviews;
    }

    public void setOverviews(String overviews) {
        this.overviews = overviews;
    }

    public String getThumnail_url() {
        return thumnail_url;
    }

    public void setThumnail_url(String thumnail_url) {
        this.thumnail_url = thumnail_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
    }

    public long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(long movie_id) {
        this.movie_id = movie_id;
    }

    public Movie(int id , String title){
        this.id = id;
        this.title = title;
    }

    public  Movie(){

    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, id);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overviews);
        values.put(MovieContract.MovieEntry.COLUMN_DATE, date);
        values.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL_URL, thumnail_url);
        values.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_IMAGE_URL, backdrop_url);
        return values;
    }
}
