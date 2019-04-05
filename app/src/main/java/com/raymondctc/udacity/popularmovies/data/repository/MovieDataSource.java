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

    @Inject
    public MovieDataSource(ApiService apiService, MovieDatabase database, int type) {
        this.apiService = apiService;
        this.database = database;
        this.type = type;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, ApiMovie> callback) {
        listState.postValue(ListState.LOADING);

        disposable.add(getMovies(String.valueOf(1))
                .subscribe(apiMovies -> {
                    // Clean up non-favourite local cache
                    database.getMovieDao().deleteNonFavMovie();

                    final int resultSize = apiMovies.results.size();
                    insertOrUpdateFromApi(apiMovies.results);

                    final String nextKey = formatNextKey(apiMovies.page + 1, resultSize);

                    final List<Movie> movieList = database.getMovieDao().getMoviesByFav(resultSize, 0);
                    final List<ApiMovie> convertedList = transformMovieListToApiMovieList(movieList);

                    listState.postValue(ListState.NORMAL);
                    callback.onResult(convertedList, null, nextKey);
                    Timber.d("@@ apiMovies, init=" + apiMovies.results + ", nextKey=" + nextKey);
                }, e -> {
                    final List<Movie> movieList = database.getMovieDao().getMoviesByFav(20, 0);
                    final List<ApiMovie> convertedList = transformMovieListToApiMovieList(movieList);
                    callback.onResult(convertedList, null, formatNextKey(1, 20));
                    listState.postValue(ListState.ERROR);
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ApiMovie> callback) {
        // Nothing
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, ApiMovie> callback) {
        listState.postValue(ListState.LOADING);

        final String[] nextKeys = parseNextKey(params.key);
        final String apiNextKey = nextKeys[0];
        final int dbNextKey = Integer.parseInt(nextKeys[1]);

        disposable.add(getMovies(apiNextKey)
                .subscribe(apiMovies -> {
                    final int resultSize = apiMovies.results.size();
                    insertOrUpdateFromApi(apiMovies.results);

                    database.getMovieDao().getMoviesByFav(apiMovies.results.size(), dbNextKey);
                    final String nextKey = formatNextKey(apiMovies.page + 1, dbNextKey + resultSize);

                    final List<Movie> movieList;
                    // End of list from server, load everything that's already fetched to DB
                    if (resultSize == 0) {
                        movieList = database.getMovieDao().getMoviesByFav(-1, dbNextKey);
                    } else {
                        movieList = database.getMovieDao().getMoviesByFav(resultSize, dbNextKey);
                    }

                    final List<ApiMovie> convertedList = transformMovieListToApiMovieList(movieList);
                    listState.postValue(ListState.NORMAL);
                    callback.onResult(convertedList, nextKey);
                    Timber.d("@@ apiMovies, loadAfter=" + apiMovies.results + ", nextKey=" + nextKey);
                }, e -> {
                    listState.postValue(ListState.ERROR);
                    Log.e(TAG, "loadAfter: ", e);
                }));
    }

    private Single<ApiMovieResponse> getMovies(String page) {
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
