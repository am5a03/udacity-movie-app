package com.raymondctc.udacity.popularmovies.di;

import com.raymondctc.udacity.popularmovies.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract MainActivity mainActivity();
}
