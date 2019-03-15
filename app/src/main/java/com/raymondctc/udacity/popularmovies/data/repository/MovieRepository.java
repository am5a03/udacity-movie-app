package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.data.MovieDataSourceFactory;

import javax.inject.Inject;

public class MovieRepository implements MovieRepositoryInterface {
    private MovieDataSourceFactory sourceFactory;

//    @Inject
    public MovieRepository(MovieDataSourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }
}
