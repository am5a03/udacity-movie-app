package com.raymondctc.udacity.popularmovies.ui.detail;

import android.view.animation.Transformation;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSourceFactory;
import com.raymondctc.udacity.popularmovies.data.repository.MovieReviewDataSource;
import com.raymondctc.udacity.popularmovies.data.repository.MovieReviewDataSourceFactory;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;
import com.raymondctc.udacity.popularmovies.models.api.ApiVideoResponse;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieReviewListViewModel extends ViewModel {

    private LiveData<PagedList<ApiReviewResponse.ApiReview>> pagedListLiveData;
    private LiveData<Integer> listState;

    @Inject
    MovieReviewDataSourceFactory movieReviewDataSourceFactory;

    @Inject
    ApiService apiService;

    @Inject
    public MovieReviewListViewModel(ApiService apiService) {
        this.apiService = apiService;
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

    public Single<ApiVideoResponse> getVideos(int id) {
        return apiService.getVideos(String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
