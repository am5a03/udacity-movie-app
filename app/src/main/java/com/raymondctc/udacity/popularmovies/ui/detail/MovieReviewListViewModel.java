package com.raymondctc.udacity.popularmovies.ui.detail;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDatabase;
import com.raymondctc.udacity.popularmovies.data.repository.MovieReviewDataSource;
import com.raymondctc.udacity.popularmovies.data.repository.MovieReviewDataSourceFactory;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;
import com.raymondctc.udacity.popularmovies.models.api.ApiVideoResponse;
import com.raymondctc.udacity.popularmovies.models.db.Movie;
import com.raymondctc.udacity.popularmovies.utils.image.Util;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieReviewListViewModel extends ViewModel implements LifecycleObserver {

    private LiveData<PagedList<ApiReviewResponse.ApiReview>> pagedListLiveData;
    private LiveData<Integer> listState;
    private final MutableLiveData<Uri> videoLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    MovieReviewDataSourceFactory movieReviewDataSourceFactory;

    @Inject
    ApiService apiService;

    @Inject
    MovieDatabase database;

    private final View.OnClickListener clickListener = v -> {
        final int id = v.getId();
        final Object o = v.getTag();

        switch (id) {
            case R.id.fav:
                final CheckBox cb = (CheckBox) v;
                final ApiMovie movie = (ApiMovie) o;
                final long now = System.currentTimeMillis();

                if (cb.isChecked()) {
                    movie.setFavTimestamp(now);
                } else {
                    movie.setFavTimestamp(0);
                }

                disposable.add(Single.just(movie)
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .subscribe(apiMovie -> {
                            final Movie dbMovie = database.getMovieDao().getMovieById(movie.id);
                            if (cb.isChecked()) {
                                dbMovie.favTimestamp = now;
                            } else {
                                dbMovie.favTimestamp = 0;
                            }
                            database.getMovieDao().updateMovie(dbMovie);
                        }));

                break;
            case R.id.play:
                final ApiVideoResponse.ApiVideo video = (ApiVideoResponse.ApiVideo) o;
                videoLiveData.postValue(Uri.parse(Util.formatYoutubeVideoPath(video.key)));
                break;
        }
    };

    @Inject
    MovieReviewListViewModel() { }

    LiveData<PagedList<ApiReviewResponse.ApiReview>> getPagedLiveData(int id) {
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

    Single<ApiVideoResponse> getVideos(int id) {
        return apiService.getVideos(String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(new Consumer<ApiVideoResponse>() {
                    @Override
                    public void accept(ApiVideoResponse apiVideoResponse) throws Exception {

                    }
                });
    }

    View.OnClickListener getClickListener() {
        return clickListener;
    }

    LiveData<Uri> getVideoUriLiveData() {
        return videoLiveData;
    }

    void saveInstaceState(Bundle bundle) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

    CompositeDisposable getDisposable() {
        return disposable;
    }
}
