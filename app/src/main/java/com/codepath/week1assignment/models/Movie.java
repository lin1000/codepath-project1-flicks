package com.codepath.week1assignment.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lin1000 on 2017/2/17.
 */

public class Movie {

    int id;
    String posterPath;
    String originalTitle;
    String overview;
    String backdropPath;
    double votedAverage;
    double popularity;
    String youtubeVideoCode;
    String releaseDate;

    public Movie(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        posterPath = jsonObject.getString("poster_path");
        originalTitle =  jsonObject.getString("original_title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        votedAverage = jsonObject.getDouble("vote_average");
        popularity = jsonObject.getDouble("popularity");
        releaseDate = jsonObject.getString("release_date");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array){
       ArrayList<Movie> results = new ArrayList<>();
        for (int x=0 ; x < array.length(); x++){
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    public double getVotedAverage() {
        return votedAverage;
    }

    public int getId() {
        return id;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getYoutubeVideoCode(){

        return youtubeVideoCode;
    }

    public void setYoutubeVideoCode(String youtubeVideoCode){
        this.youtubeVideoCode =  youtubeVideoCode;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
