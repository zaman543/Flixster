package com.codepath.nzaman.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private String posterPath;
    private String title;
    private String overview;

    public Movie(JSONObject jsonObjMovie, String finalurl) throws JSONException {
        posterPath = finalurl + jsonObjMovie.getString("poster_path");
        title = jsonObjMovie.getString("title");
        overview = jsonObjMovie.getString("overview");
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
}
