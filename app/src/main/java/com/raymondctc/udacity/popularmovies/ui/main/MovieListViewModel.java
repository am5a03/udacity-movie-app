package com.raymondctc.udacity.popularmovies.ui.main;

import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSource;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSourceFactory;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDatabase;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class MovieListViewModel extends ViewModel {
    private LiveData<PagedList<ApiMovie>> pagedLiveData;
    private LiveData<Integer> listState;

    @Inject
    MovieDataSourceFactory movieDataSourceFactory;

    @Inject
    MovieDatabase movieDatabase;

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

            listState = Transformations.switchMap(movieDataSourceFactory.getSourceMutableLiveData(), MovieDataSource::getListState);

        }
        return pagedLiveData;
    }

    public LiveData<Integer> getListState() {
        return listState;
    }

    public void refreshByType(int type) {
        movieDataSourceFactory.setRefreshType(type);
        movieDataSourceFactory.getSourceMutableLiveData()
                .getValue().invalidate();
    }

    public void refresh() {
        movieDataSourceFactory.getSourceMutableLiveData()
                .getValue().invalidate();
    }
}
