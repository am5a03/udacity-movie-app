package com.raymondctc.udacity.popularmovies.data;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

@Singleton
public class MovieDataSourceFactory extends DataSource.Factory<Integer, ApiMovie> {

    private final ApiService apiService;
    private final MutableLiveData<MovieDataSource> sourceMutableLiveData = new MutableLiveData<>();

    @Inject
    public MovieDataSourceFactory(final ApiService apiService) {
        this.apiService = apiService;
    }


    @NonNull
    @Override
    public DataSource<Integer, ApiMovie> create() {
        final MovieDataSource movieDataSource = new MovieDataSource(apiService);
        sourceMutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}
