package com.raymondctc.udacity.popularmovies.data.repository;

import android.util.Log;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovieResponse;
import com.raymondctc.udacity.popularmovies.models.db.Movie;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieDataSource extends PageKeyedDataSource<String, ApiMovie> {
    private static final String TAG = "MovieDataSource";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<Integer> listState = new MutableLiveData<>();

    private MovieDatabase database;
    private ApiService apiService;
    private int type;

    public static final int TYPE_BY_RATING = 0;
    public static final int TYPE_BY_POPULARITY = 1;
    public static final int TYPE_BY_FAVOURITES = 2;

    private static final int DB_LIMIT = 20;

    @Inject
    public MovieDataSource(ApiService apiService, MovieDatabase database, int type) {
        this.apiService = apiService;
        this.database = database;
        this.type = type;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, ApiMovie> callback) {
        listState.postValue(ListState.LOADING);

        final Disposable d;
        if (type == TYPE_BY_FAVOURITES) {
            d = getMoviesViaDb(DB_LIMIT, 0)
                    .subscribe(movies -> {
                        final List<ApiMovie> convertedList = transformMovieListToApiMovieList(movies);
                        listState.postValue(ListState.NORMAL);
                        callback.onResult(convertedList, null, String.valueOf(movies.size()));
                    }, e -> listState.postValue(ListState.ERROR));
        } else {
            d = getMoviesViaApi(String.valueOf(1))
                    .subscribe(apiMovies -> {
                        // Clean up non-favourite local cache
                        database.getMovieDao().deleteNonFavMovie();
                        final List<Movie> movieList = insertOrUpdateFromApi(apiMovies.results);
                        listState.postValue(ListState.NORMAL);
                        callback.onResult(transformMovieListToApiMovieList(movieList), null, String.valueOf(apiMovies.page + 1));
                        Timber.d("@@ apiMovies, init=" + apiMovies.results);
                    }, e -> {
                        final List<Movie> movieList = database.getMovieDao().getMovies(DB_LIMIT, 0);
                        final List<ApiMovie> convertedList = transformMovieListToApiMovieList(movieList);
                        callback.onResult(convertedList, null, formatNextKey(1, DB_LIMIT));
                        listState.postValue(ListState.ERROR);
                    });
        }

        disposable.add(d);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ApiMovie> callback) {
        // Nothing
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ApiMovie> callback) {
        listState.postValue(ListState.LOADING);


        final Disposable d;

        if (type == TYPE_BY_FAVOURITES) {
            final int dbNextKey = Integer.parseInt(params.key);
            d = getMoviesViaDb(DB_LIMIT, dbNextKey)
                    .subscribe(movies -> {
                        final List<ApiMovie> convertedList = transformMovieListToApiMovieList(movies);
                        listState.postValue(ListState.NORMAL);
                        callback.onResult(convertedList, String.valueOf(movies.size() + dbNextKey));
                    }, e -> listState.postValue(ListState.ERROR));
        } else {
            d = getMoviesViaApi(params.key)
                    .subscribe(apiMovies -> {
                        final List<Movie> movieList = insertOrUpdateFromApi(apiMovies.results);
                        listState.postValue(ListState.NORMAL);
                        callback.onResult(transformMovieListToApiMovieList(movieList), String.valueOf(apiMovies.page + 1));
                        Timber.d("@@ apiMovies, loadAfter=" + apiMovies.results);
                    }, e -> {
                        listState.postValue(ListState.ERROR);
                        Log.e(TAG, "loadAfter: ", e);
                    });
        }

        disposable.add(d);
    }

    private Single<List<Movie>> getMoviesViaDb(final int limit, final int offset) {
        return Single.just(database.getMovieDao().getFavMovies(limit, offset));
    }

    private Single<ApiMovieResponse> getMoviesViaApi(String page) {
        if (TYPE_BY_POPULARITY == type) {
            return apiService.getPopularMovies(Integer.parseInt(page))
                    ;
        } else if (TYPE_BY_RATING == type) {
            return apiService.getTopRatedMovies(Integer.parseInt(page))
                    ;
        } else {
            throw new IllegalArgumentException("Not a correct type");
        }
    }

    /**
     * Check if a movie is already there, if yes, do update, if not, insert
     * @param results
     */
    private List<Movie> insertOrUpdateFromApi(final List<ApiMovie> results) {
        final List<Movie> list = new ArrayList<>();
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
            list.add(movie);
        }
        return list;
    }

    /**
     * Parse the next page key
     * @param nextKey
     * @return
     */
    private String[] parseNextKey(String nextKey) {
        return nextKey.split(",");
    }

    /**
     * Genearte a next page key, containing DB offset and API page
     * @param apiPage
     * @param dbPage
     * @return
     */
    private String formatNextKey(int apiPage, int dbPage) {
        return apiPage + "," + dbPage;
    }

    /**
     * Create a database {@link Movie} object from {@link ApiMovie}
     * @param apiMovie
     * @return
     */
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

    /**
     * Converting DB fetched items to UI displaying models
     * @param movie
     * @return
     */
    private ApiMovie transformMovieToApiMovie(Movie movie) {
        final ApiMovie apiMovie = new ApiMovie();
        apiMovie.id = movie.movieId;
        apiMovie.originalTitle = movie.originalTitle;
        apiMovie.overview = movie.overview;
        apiMovie.posterPath = movie.posterPath;
        apiMovie.releaseDate = movie.releaseDate;
        apiMovie.voteAverage = movie.voteAverage;
        apiMovie.setFavTimestamp(movie.favTimestamp);
        return apiMovie;
    }

    private List<ApiMovie> transformMovieListToApiMovieList(List<Movie> movieList) {
        final List<ApiMovie> convertedList = new ArrayList<>();
        for (int i = 0; i < movieList.size(); i++) {
            convertedList.add(transformMovieToApiMovie(movieList.get(i)));
        }

        return convertedList;
    }

    public MutableLiveData<Integer> getListState() {
        return listState;
    }

}
