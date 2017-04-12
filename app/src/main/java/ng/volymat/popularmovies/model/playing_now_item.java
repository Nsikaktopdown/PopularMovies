package ng.volymat.popularmovies.model;

import java.util.ArrayList;

/**
 * Created by Nsikak  Thompson on 12/8/2016.
 */

public class playing_now_item {
    private int id;
    private  String movie_title;
    private String poster_path;
    private String overview;
    private String vote_average;
    private String backdrop;
    private String release_date;



    private ArrayList<Integer> genre;

    public playing_now_item(){

    }


    public playing_now_item(int id, String movie_title, String poster_path, String overview, String vote_average, String backdrop, ArrayList<Integer> genre, String release_date) {
        this.id = id;
        this.movie_title = movie_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.backdrop= backdrop;
        this.release_date = release_date;

    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public ArrayList<Integer> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Integer> genre) {
        this.genre = genre;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
