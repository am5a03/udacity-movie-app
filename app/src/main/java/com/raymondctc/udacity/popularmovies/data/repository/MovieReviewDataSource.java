package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

@Singleton
public class MovieReviewDataSource extends PageKeyedDataSource<Integer, ApiReviewResponse.ApiReview> {

    private ApiService apiService;
    private int id;

    @Inject
    public MovieReviewDataSource(ApiService apiService, int id) {
        this.apiService = apiService;
        this.id = id;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ApiReviewResponse.ApiReview> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiReviewResponse.ApiReview> callback) {
        // Do nothing
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiReviewResponse.ApiReview> callback) {

    }
}
