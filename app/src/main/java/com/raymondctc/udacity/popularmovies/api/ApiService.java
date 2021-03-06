package com.raymondctc.udacity.popularmovies.api;

import com.raymondctc.udacity.popularmovies.BuildConfig;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovieResponse;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;
import com.raymondctc.udacity.popularmovies.models.api.ApiVideoResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface ApiService {

    @GET("movie/popular")
    Single<ApiMovieResponse> getPopularMovies(@Query("page") final int page);

    @GET("movie/top_rated")
    Single<ApiMovieResponse> getTopRatedMovies(@Query("page") final int page);

    @GET("movie/{id}/videos")
    Single<ApiVideoResponse> getVideos(@Path("id") final String id);

    @GET("movie/{id}/reviews")
    Single<ApiReviewResponse> getReviews(@Path("id") final String id, @Query("page") final int page);
}
