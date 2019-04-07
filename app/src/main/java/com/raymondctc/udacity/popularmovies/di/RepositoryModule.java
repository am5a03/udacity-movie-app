package com.raymondctc.udacity.popularmovies.di;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSource;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDatabase;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

//    @Provides
//    @Singleton
//    @NonNull
//    public MovieDataSource provideMovieDataSource(ApiService apiService, MovieDatabase database, int type) {
//        return new MovieDataSource(apiService, database, type);
//    }
}
