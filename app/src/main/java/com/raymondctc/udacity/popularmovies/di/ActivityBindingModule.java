package com.raymondctc.udacity.popularmovies.di;

import com.raymondctc.udacity.popularmovies.ui.MainActivity;
import com.raymondctc.udacity.popularmovies.ui.MovieListViewModel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
//            modules = {
//                    ViewModelModule.class
//            }
    )
    abstract MainActivity mainActivity();
}
