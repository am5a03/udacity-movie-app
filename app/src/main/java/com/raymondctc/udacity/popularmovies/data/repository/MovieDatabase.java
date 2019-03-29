package com.raymondctc.udacity.popularmovies.data.repository;

import com.raymondctc.udacity.popularmovies.data.db.MovieDao;
import com.raymondctc.udacity.popularmovies.models.db.Movie;

import javax.inject.Singleton;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    static final int VERSION = 1;

    public abstract MovieDao getMovieDao();
}
