package com.raymondctc.udacity.popularmovies.data;

import android.util.Log;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovieResponse;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieDataSource extends PageKeyedDataSource<Integer, ApiMovie> {
    private static final String TAG = "MovieDataSource";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<Integer> listState = new MutableLiveData<>();

    private ApiService apiService;
    private int type;

    public static final int TYPE_BY_RATING = 0;
    public static final int TYPE_BY_POPULARITY = 1;

    @Inject
    public MovieDataSource(ApiService apiService, int type) {
        this.apiService = apiService;
        this.type = type;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ApiMovie> callback) {
        listState.postValue(ListState.LOADING);
        disposable.add(getMovies(1).subscribe(apiMovies -> {
            listState.postValue(ListState.NORMAL);
            callback.onResult(apiMovies.results, null, apiMovies.page + 1);
            Timber.d("@@ apiMovies=" + apiMovies.results);
        }, e -> {
            listState.postValue(ListState.ERROR);
        }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiMovie> callback) {
        // Nothing
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ApiMovie> callback) {
        listState.postValue(ListState.LOADING);
        disposable.add(getMovies(params.key).subscribe(apiMovies -> {
            listState.postValue(ListState.NORMAL);
            Log.d(TAG, "loadAfter: ");
            callback.onResult(apiMovies.results, apiMovies.page + 1);
        }, e -> {
            listState.postValue(ListState.ERROR);
            Log.e(TAG, "loadAfter: ", e);
        }));
    }

    private Single<ApiMovieResponse> getMovies(int page) {
        if (TYPE_BY_POPULARITY == type) {
            return apiService.getPopularMovies(page)
                    ;
        } else if (TYPE_BY_RATING == type) {
            return apiService.getTopRatedMovies(page)
                    ;
        } else {
            throw new IllegalArgumentException("Not a correct type");
        }
    }

    public MutableLiveData<Integer> getListState() {
        return listState;
    }

    public static class ListState {
        public static final int INIT_LOAD = 0;
        public static final int LOADING = 1;
        public static final int NORMAL = 2;
        public static final int ERROR = 3;
    }
}
