package com.codepath.nzaman.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.nzaman.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends YouTubeBaseActivity {
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=";

    Movie movie;
    TextView tvTitleDetails;
    TextView tvOverviewDetails;
    RatingBar rbVoteAverage;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        tvOverviewDetails = findViewById(R.id.tvOverviewDetails);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        youTubePlayerView = findViewById(R.id.player);

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitleDetails.setText(movie.getTitle());
        tvOverviewDetails.setText(movie.getOverview());
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage/2.0f);

        AsyncHttpClient client = new AsyncHttpClient();
        String video_url_full = VIDEOS_URL + getString(R.string.moviedb);
        client.get(String.format(video_url_full, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0)
                        return;

                    int videoIndex = 0;
                    int numTry = 1;
                    String confirm_yt = results.getJSONObject(videoIndex).getString("site");
                    while(!confirm_yt.equals("YouTube") && results.length() > numTry){
                        videoIndex++;
                        numTry++;
                        confirm_yt = results.getJSONObject(videoIndex).getString("site");
                    }
                    if(confirm_yt.equals("YouTube")) {
                        String youtube_key = results.getJSONObject(0).getString("key");
                        Log.d("MovieDetailsYTKey", youtube_key);
                        initializeYoutube(youtube_key);
                    } else {
                        Log.e("MovieDetailsActivity", "YouTube video not found.");
                    }
                } catch (JSONException e) {
                    Log.e("MovieDetailsActivity", "failed to parse JSON Array");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("MovieDetailsActivity", "onFailure - get request failed");
            }
        });
    }

    private void initializeYoutube(String youtube_key) {
        final String videoId = youtube_key;

        YouTubePlayerView playerView = (YouTubePlayerView)findViewById(R.id.player);

        playerView.initialize(getString(R.string.youtube), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("MovieDetailsActivity", "onInitializationSuccess: ");
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                //log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player: ");
            }
        });
    }
}