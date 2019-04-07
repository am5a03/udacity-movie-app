package com.raymondctc.udacity.popularmovies.ui.main;

import android.app.Activity;
import android.content.Intent;

import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSource;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSourceFactory;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDatabase;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.models.app.MovieData;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import static com.raymondctc.udacity.popularmovies.ui.detail.MovieDetailActivity.KEY_MOVIE_DETAIL;
import static com.raymondctc.udacity.popularmovies.ui.detail.MovieDetailActivity.KEY_MOVIE_POSITION;
import static com.raymondctc.udacity.popularmovies.ui.main.MainActivity.MOVIE_DETAIL_ACTIVITY_REQUEST_CODE;

public class MovieListViewModel extends ViewModel {
    private LiveData<PagedList<ApiMovie>> pagedLiveData;
    private LiveData<Integer> listState;
    private final MutableLiveData<Integer> removeItemLiveData = new MutableLiveData<>();

    @Inject
    MovieDataSourceFactory movieDataSourceFactory;

    @Inject
    MovieDatabase movieDatabase;

    @Inject
    MovieListViewModel() {

    }

    LiveData<PagedList<ApiMovie>> getPagedLiveData() {
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

    LiveData<Integer> getListState() {
        return listState;
    }

    void refreshByType(int type) {
        movieDataSourceFactory.setRefreshType(type);
        movieDataSourceFactory.getSourceMutableLiveData()
                .getValue().invalidate();
    }

    void refresh() {
        movieDataSourceFactory.getSourceMutableLiveData()
                .getValue().invalidate();
    }

    void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (data != null && requestCode == MOVIE_DETAIL_ACTIVITY_REQUEST_CODE) {
            final int pos = data.getIntExtra(KEY_MOVIE_POSITION, -1);
            final ApiMovie apiMovie = data.getParcelableExtra(KEY_MOVIE_DETAIL);
            if (pos >= 0 && apiMovie != null) {
                pagedLiveData.getValue().get(pos).setFavTimestamp(apiMovie.getFavTimestamp());
                if (apiMovie.getFavTimestamp() == 0 && movieDataSourceFactory.getType() == MovieDataSource.TYPE_BY_FAVOURITES) {
                    removeItemLiveData.postValue(pos);
                }
            }
        }
    }

    public MutableLiveData<Integer> getRemoveItemLiveData() {
        return removeItemLiveData;
    }
}
