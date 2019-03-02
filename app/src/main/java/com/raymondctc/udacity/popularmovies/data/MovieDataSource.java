package com.raymondctc.udacity.popularmovies.data;

import com.raymondctc.udacity.popularmovies.models.app.MovieData;

import java.util.List;

public interface MovieDataSource {
    List<MovieData> getRemoteMovieData();
    List<MovieData> getLocalMovieData();
}
