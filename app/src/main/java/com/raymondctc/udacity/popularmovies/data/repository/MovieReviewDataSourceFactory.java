package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

@Singleton
public class MovieReviewDataSourceFactory extends DataSource.Factory<Integer, ApiReviewResponse.ApiReview> {

    private ApiService apiService;
    private int id;
    private final MutableLiveData<MovieReviewDataSource> mutableLiveData = new MutableLiveData<>();

    @Inject
    public MovieReviewDataSourceFactory(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public DataSource<Integer, ApiReviewResponse.ApiReview> create() {
        final MovieReviewDataSource dataSource = new MovieReviewDataSource(apiService, id);
        mutableLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<MovieReviewDataSource> getSourceMutableLiveData() {
        return mutableLiveData;
    }
}
