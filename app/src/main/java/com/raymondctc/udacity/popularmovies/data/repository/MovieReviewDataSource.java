package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.disposables.CompositeDisposable;

@Singleton
public class MovieReviewDataSource extends PageKeyedDataSource<Integer, ApiReviewResponse.ApiReview> {

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<Integer> listState = new MutableLiveData<>();
    private ApiService apiService;
    private int id;

    @Inject
    public MovieReviewDataSource(ApiService apiService, int id) {
        this.apiService = apiService;
        this.id = id;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ApiReviewResponse.ApiReview> callback) {
        disposable.add(apiService.getReviews(String.valueOf(id), 1).subscribe(apiReviewResponse -> {
            listState.postValue(ListState.NORMAL);
            callback.onResult(apiReviewResponse.results, null, apiReviewResponse.page + 1);
        }, e -> {
            listState.postValue(ListState.ERROR);
        }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiReviewResponse.ApiReview> callback) {
        // Do nothing
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiReviewResponse.ApiReview> callback) {
        listState.postValue(ListState.LOADING);
        disposable.add(apiService.getReviews(String.valueOf(id), params.key).subscribe(apiReviewResponse -> {
            listState.postValue(ListState.NORMAL);
            callback.onResult(apiReviewResponse.results,apiReviewResponse.page + 1);
        }, e -> {
            listState.postValue(ListState.ERROR);
        }));
    }

    public MutableLiveData<Integer> getListState() {
        return listState;
    }
}
