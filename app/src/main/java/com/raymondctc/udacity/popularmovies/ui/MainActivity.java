package com.raymondctc.udacity.popularmovies.ui;

import android.os.Bundle;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;


public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

//    @Inject
    MovieListViewModel movieListViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieListViewModel.class);
        movieListViewModel.getPagedLiveData()
                .observe(this, apiMovie -> {

                });

    }
}
