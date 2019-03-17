package com.raymondctc.udacity.popularmovies.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.utils.image.Util;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import dagger.android.support.DaggerAppCompatActivity;

public class MovieDetailActivity extends DaggerAppCompatActivity {

    public static final String KEY_MOVIE_DETAIL = "movie_detail";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        final ApiMovie apiMovie = getIntent().getParcelableExtra(KEY_MOVIE_DETAIL);

        final ImageView thumbnail = findViewById(R.id.movie_thumbnail);
        final TextView title = findViewById(R.id.title);
        final TextView overview = findViewById(R.id.overview);
        final TextView year = findViewById(R.id.year);
        final TextView rating = findViewById(R.id.rating);
        final TextView length = findViewById(R.id.length);

        Picasso.get().load(Util.formatImagePath(apiMovie.posterPath))
                .into(thumbnail);

        title.setText(apiMovie.originalTitle);
        overview.setText(apiMovie.overview);
        year.setText(apiMovie.releaseDate);
        rating.append(String.valueOf(apiMovie.voteAverage));
        rating.append("/10");
    }
}
