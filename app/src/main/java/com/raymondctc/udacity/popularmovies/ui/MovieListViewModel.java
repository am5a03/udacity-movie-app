package com.raymondctc.udacity.popularmovies.ui;

import com.raymondctc.udacity.popularmovies.data.MovieDataSourceFactory;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            PagedList.Config pagedListConfig =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(true)
                            .setInitialLoadSizeHint(10)
                            .setPageSize(20).build();
            pagedLiveData = new LivePagedListBuilder<>(movieDataSourceFactory, pagedListConfig)
                    .setFetchExecutor(Executors.newFixedThreadPool(2))
                    .build();
        }
        return pagedLiveData;
    }
}
