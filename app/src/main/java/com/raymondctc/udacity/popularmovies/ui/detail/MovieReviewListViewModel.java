package com.raymondctc.udacity.popularmovies.ui.detail;

import android.view.animation.Transformation;

import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSourceFactory;
import com.raymondctc.udacity.popularmovies.data.repository.MovieReviewDataSource;
import com.raymondctc.udacity.popularmovies.data.repository.MovieReviewDataSourceFactory;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class MovieReviewListViewModel extends ViewModel {

    private LiveData<PagedList<ApiReviewResponse.ApiReview>> pagedListLiveData;
    private LiveData<Integer> listState;

    @Inject
    MovieReviewDataSourceFactory movieReviewDataSourceFactory;

    @Inject
    public MovieReviewListViewModel() {

    }

    public LiveData<PagedList<ApiReviewResponse.ApiReview>> getPagedLiveData(int id) {
        if (pagedListLiveData == null) {
            movieReviewDataSourceFactory.setId(id);
            PagedList.Config pagedListConfig =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(true)
                            .setInitialLoadSizeHint(10)
                            .setPageSize(20).build();
            pagedListLiveData = new LivePagedListBuilder<>(movieReviewDataSourceFactory, pagedListConfig)
                    .setFetchExecutor(Executors.newFixedThreadPool(2))
                    .build();
            listState = Transformations.switchMap(movieReviewDataSourceFactory.getSourceMutableLiveData(), MovieReviewDataSource::getListState);
        }

        return pagedListLiveData;
    }

}
