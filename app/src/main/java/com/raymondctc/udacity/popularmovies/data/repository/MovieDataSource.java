package com.raymondctc.udacity.popularmovies.data.repository;

import android.util.Log;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovieResponse;
import com.raymondctc.udacity.popularmovies.models.db.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieDataSource extends PageKeyedDataSource<Integer, ApiMovie> {
    private static final String TAG = "MovieDataSource";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<Integer> listState = new MutableLiveData<>();

    private MovieDatabase database;
    private ApiService apiService;
    private int type;

    public static final int TYPE_BY_RATING = 0;
    public static final int TYPE_BY_POPULARITY = 1;

    @Inject
    public MovieDataSource(ApiService apiService, MovieDatabase database, int type) {
        this.apiService = apiService;
        this.database = database;
        this.type = type;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ApiMovie> callback) {
        listState.postValue(ListState.LOADING);

        disposable.add(getMovies(1)
                .subscribe(apiMovies -> {
                    database.getMovieDao().deleteNonFavMovie();
                    insertOrUpdateFromApi(apiMovies.results);
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
        disposable.add(getMovies(params.key)
                .subscribe(apiMovies -> {
                    insertOrUpdateFromApi(apiMovies.results);
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

    /**
     *
     * @param results
     */
    private void insertOrUpdateFromApi(final List<ApiMovie> results) {
        for (int i = 0; i < results.size(); i++) {
            final ApiMovie apiMovie = results.get(i);
            Movie movie = database.getMovieDao().getMovieById(apiMovie.id);
            if (movie == null) {
                movie = createMovieFromApiMovie(apiMovie);
                database.getMovieDao().insertAll(movie);
            } else {
                movie = updateMovie(movie, apiMovie);
                database.getMovieDao().updateMovie(movie);
            }
        }
    }

    private Movie createMovieFromApiMovie(ApiMovie apiMovie) {
        final Movie movie = new Movie();
        updateMovie(movie, apiMovie);
        movie.favTimestamp = 0;
        return movie;
    }

    private Movie updateMovie(Movie movie, ApiMovie apiMovie) {
        movie.originalTitle = apiMovie.originalTitle;
        movie.overview = apiMovie.overview;
        movie.posterPath = apiMovie.posterPath;
        movie.releaseDate = apiMovie.releaseDate;
        movie.voteAverage = apiMovie.voteAverage;

        movie.movieId = apiMovie.id;
        return movie;
    }

    public MutableLiveData<Integer> getListState() {
        return listState;
    }

}
