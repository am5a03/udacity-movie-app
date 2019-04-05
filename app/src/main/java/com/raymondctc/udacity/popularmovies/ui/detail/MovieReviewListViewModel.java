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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.annotation.NonNull;
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
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MovieReviewListViewModel extends ViewModel implements LifecycleObserver {
    public static final String KEY_VIDEOS = "videos";


    private LiveData<PagedList<ApiReviewResponse.ApiReview>> pagedListLiveData;
    private LiveData<Integer> listState;
    private final MutableLiveData<List<ApiVideoResponse.ApiVideo>> videoListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Uri> videoUriLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final ArrayList<ApiVideoResponse.ApiVideo> videos = new ArrayList<>();

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
                videoUriLiveData.postValue(Uri.parse(Util.formatYoutubeVideoPath(video.key)));
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

    void getVideos(int id) {
        disposable.add(
                apiService.getVideos(String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiVideoResponse -> {
                    videos.clear();
                    videos.addAll(apiVideoResponse.results);
                    videoListLiveData.postValue(videos);
                })
        );
    }

    View.OnClickListener getClickListener() {
        return clickListener;
    }

    LiveData<Uri> getVideoUriLiveData() {
        return videoUriLiveData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }


    MutableLiveData<List<ApiVideoResponse.ApiVideo>> getVideoListLiveData() {
        return videoListLiveData;
    }
}
