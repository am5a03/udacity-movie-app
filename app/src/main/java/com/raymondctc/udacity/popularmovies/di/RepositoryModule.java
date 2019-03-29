package com.raymondctc.udacity.popularmovies.di;

import com.raymondctc.udacity.popularmovies.api.ApiService;
import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSource;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    @NonNull
    public MovieDataSource provideMovieDataSource(ApiService apiService, int type) {
        return new MovieDataSource(apiService, type);
    }
}
