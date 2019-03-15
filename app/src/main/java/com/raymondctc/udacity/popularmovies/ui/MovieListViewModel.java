package com.raymondctc.udacity.popularmovies.ui;

import com.raymondctc.udacity.popularmovies.data.MovieDataSourceFactory;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class MovieListViewModel extends ViewModel {
    private LiveData<PagedList<ApiMovie>> pagedLiveData;

    @Inject
    MovieDataSourceFactory movieDataSourceFactory;

    @Inject
    public MovieListViewModel() {

    }

    public LiveData<PagedList<ApiMovie>> getPagedLiveData() {
        if (pagedLiveData == null) {
            PagedList.Config config = new PagedList.Config.Builder().build();
            pagedLiveData = new LivePagedListBuilder<>(movieDataSourceFactory, config).build();
        }
        return pagedLiveData;
    }
}
