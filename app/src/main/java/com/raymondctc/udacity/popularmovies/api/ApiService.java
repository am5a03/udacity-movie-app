package com.raymondctc.udacity.popularmovies.api;

import com.raymondctc.udacity.popularmovies.BuildConfig;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovieResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("movie/popular")
    Single<ApiMovieResponse> getPopularMovies();
}
