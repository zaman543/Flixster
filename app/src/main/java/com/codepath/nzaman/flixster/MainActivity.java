package com.codepath.nzaman.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.nzaman.flixster.adapters.MovieAdapter;
import com.codepath.nzaman.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    //embedding an api key
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "Main Activity";
    public static final String CONFIGURATIONS = "https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String MOVIETAG = "Fetching Config, main activity";

    List<String> sizes = new ArrayList<>();
    String secureBaseURL;
    List<Movie> movies;
    String finalurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        movies = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(CONFIGURATIONS, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                Log.d(MOVIETAG, "onSuccess");
                JSONObject jsonObjMovieConfig = response.jsonObject;
                try {
                    JSONObject images = jsonObjMovieConfig.getJSONObject("images");
                    JSONArray posterSizes = images.getJSONArray("poster_sizes");
                    //global vars = BAD; pass as parameters??
                    secureBaseURL = images.getString("secure_base_url");
                    Log.i(MOVIETAG, "posterSizes: " + posterSizes.toString());
                    sizes = jsonArrayToStrArr(posterSizes);
                    finalurl = secureBaseURL + sizes.get(sizes.size()/2);

                    client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON response) {
                            Log.d(TAG, "onSuccess");
                            JSONObject jsonObjMovie = response.jsonObject;
                            try {
                                JSONArray results = jsonObjMovie.getJSONArray("results");
                                Log.i(TAG, "Results: " + results.toString());
                                movies.addAll(Movie.fromJsonArray(results, finalurl));
                                movieAdapter.notifyDataSetChanged();
                                Log.i(TAG, "Movies: " + movies.size());
                            } catch (JSONException e) {
                                Log.e(TAG, "onSuccess: Hit json exception", e);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d(TAG, "onFailure");
                        }
                    });


                } catch (JSONException e) {
                    Log.e(MOVIETAG, "Configurations API: Hit json exception", e);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(MOVIETAG, "onFailure");
            }
        });
    }

    private static List<String> jsonArrayToStrArr(JSONArray stringJsonArray) throws JSONException {
        List<String> stringList= new ArrayList<>();
        for(int i = 0; i < stringJsonArray.length(); i++){
            String addString = stringJsonArray.getString(i);
            stringList.add(addString);
        }
        return stringList;
    }
}