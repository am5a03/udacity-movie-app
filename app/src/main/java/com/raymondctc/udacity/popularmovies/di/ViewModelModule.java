package com.raymondctc.udacity.popularmovies.di;

import com.raymondctc.udacity.popularmovies.ui.detail.MovieReviewListViewModel;
import com.raymondctc.udacity.popularmovies.ui.main.MovieListViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel.class)
    abstract ViewModel bindMovieListViewModel(MovieListViewModel movieListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieReviewListViewModel.class)
    abstract ViewModel bindMovieReviewListViewModel(MovieReviewListViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(MovieAppViewModelFactory factory);
}
