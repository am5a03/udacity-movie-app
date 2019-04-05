package com.raymondctc.udacity.popularmovies.di;

import android.app.Application;

import com.raymondctc.udacity.popularmovies.MainApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                AppModule.class,
                ViewModelModule.class,
                RetrofitModule.class,
                RepositoryModule.class,
                RoomModule.class,
                ActivityBindingModule.class
        }
)
public interface AppComponent extends AndroidInjector<MainApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MainApplication> {

    }
}
