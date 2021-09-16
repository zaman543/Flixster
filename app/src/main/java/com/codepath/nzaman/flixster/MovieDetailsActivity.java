package com.codepath.nzaman.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.nzaman.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitleDetails;
    TextView tvOverviewDetails;
    RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitleDetails = (TextView)findViewById(R.id.tvTitleDetails);
        tvOverviewDetails = (TextView)findViewById(R.id.tvOverviewDetails);
        rbVoteAverage = (RatingBar)findViewById(R.id.rbVoteAverage);

        movie = (Movie)Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitleDetails.setText(movie.getTitle());
        tvOverviewDetails.setText(movie.getOverview());
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage/2.0f);
    }
}