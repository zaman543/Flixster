package com.codepath.nzaman.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    public String posterPath;
    public String title;
    public String overview;
    public Double voteAverage;
    public Integer id;

    public Movie() {}
    public Movie(JSONObject jsonObjMovie, String finalurl) throws JSONException {
        voteAverage = jsonObjMovie.getDouble("vote_average");
        posterPath = finalurl + jsonObjMovie.getString("poster_path");
        title = jsonObjMovie.getString("title");
        overview = jsonObjMovie.getString("overview");
        id = jsonObjMovie.getInt("id");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray, String finalurl) throws JSONException {
        List<Movie> movieList= new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++){
            Movie addMovie = new Movie(movieJsonArray.getJSONObject(i), finalurl);
            movieList.add(addMovie);
        }
        return movieList;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() { return voteAverage; }

    public Integer getId() { return id; }

}
