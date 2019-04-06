package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.models.db.Movie;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import retrofit2.http.DELETE;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie LIMIT :limit OFFSET :offset")
    List<Movie> getMovies(final int limit, final int offset);

    @Query("SELECT * FROM movie WHERE fav_timestamp != 0 ORDER BY fav_timestamp DESC, id LIMIT :limit OFFSET :offset")
    List<Movie> getFavMovies(final int limit, final int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Movie... movie);

    @Update
    void updateMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE movie_id = :movieId")
    Movie getMovieById(int movieId);

    @Query("DELETE FROM movie where fav_timestamp == 0")
    int deleteNonFavMovie();

    @Query("SELECT COUNT(1) FROM movie WHERE fav_timestamp != 0")
    int countFavMovies();
}
