package com.raymondctc.udacity.popularmovies.data;

import com.raymondctc.udacity.popularmovies.models.app.MovieData;

import java.util.List;

public interface MovieDataSourceInterface {
    List<MovieData> getRemoteMovieData();
    List<MovieData> getLocalMovieData();
}
