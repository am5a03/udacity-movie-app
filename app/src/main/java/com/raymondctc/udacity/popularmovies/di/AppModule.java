package com.raymondctc.udacity.popularmovies.di;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public Context provideContext(Application application) {
        return application.getApplicationContext();
    }
}