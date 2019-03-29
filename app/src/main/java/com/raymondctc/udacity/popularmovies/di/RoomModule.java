package com.raymondctc.udacity.popularmovies.di;


import com.raymondctc.udacity.popularmovies.MainApplication;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDao;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDatabase;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
class RoomModule {

    @NonNull
    @Singleton
    @Provides
    MovieDatabase provieMovieDatabase(MainApplication application) {
        return Room.databaseBuilder(application, MovieDatabase.class, "movie-db").build();
    }

    @NonNull
    @Singleton
    @Provides
    MovieDao provideMovieDao(MovieDatabase database) {
        return database.getMovieDao();
    }
}
