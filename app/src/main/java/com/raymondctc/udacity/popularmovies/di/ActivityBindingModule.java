package com.raymondctc.udacity.popularmovies.di;

import com.raymondctc.udacity.popularmovies.ui.main.MainActivity;
import com.raymondctc.udacity.popularmovies.ui.detail.MovieDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract MainActivity mainActivity();


    @ActivityScoped
    @ContributesAndroidInjector()
    abstract MovieDetailActivity movieDetailActivity();
}
