package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import static com.raymondctc.udacity.popularmovies.data.repository.MovieDataSource.TYPE_BY_POPULARITY;

@Singleton
public class MovieDataSourceFactory extends DataSource.Factory<Integer, ApiMovie> {

    private final ApiService apiService;
    private final MutableLiveData<MovieDataSource> sourceMutableLiveData = new MutableLiveData<>();
    private int type = TYPE_BY_POPULARITY;

    @Inject
    public MovieDataSourceFactory(final ApiService apiService) {
        this.apiService = apiService;
    }


    @NonNull
    @Override
    public DataSource<Integer, ApiMovie> create() {
        final MovieDataSource movieDataSource = new MovieDataSource(apiService, type);
        sourceMutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getSourceMutableLiveData() {
        return sourceMutableLiveData;
    }

    public void setRefreshType(int type) {
        this.type = type;
    }
}
