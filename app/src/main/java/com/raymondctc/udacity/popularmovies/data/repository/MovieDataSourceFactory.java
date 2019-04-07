package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import timber.log.Timber;

import static com.raymondctc.udacity.popularmovies.data.repository.MovieDataSource.TYPE_BY_POPULARITY;

@Singleton
public class MovieDataSourceFactory extends DataSource.Factory<String, ApiMovie> {

    private final MovieDatabase database;
    private final ApiService apiService;
    private final List<ApiMovie> backingList = new CopyOnWriteArrayList<>();
    private final MutableLiveData<MovieDataSource> sourceMutableLiveData = new MutableLiveData<>();
    private int type = TYPE_BY_POPULARITY;

    @Inject
    MovieDataSourceFactory(final ApiService apiService, final MovieDatabase database) {
        this.apiService = apiService;
        this.database = database;
    }


    @NonNull
    @Override
    public DataSource<String, ApiMovie> create() {
        Timber.d("Factory DS=" + this);
        final MovieDataSource movieDataSource = new MovieDataSource(apiService, database, type,
                backingList);
        sourceMutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getSourceMutableLiveData() {
        return sourceMutableLiveData;
    }

    public void setRefreshType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void removeItemFromBackingList(int pos) {
        if (pos < this.backingList.size()) this.backingList.remove(pos);
    }
}
