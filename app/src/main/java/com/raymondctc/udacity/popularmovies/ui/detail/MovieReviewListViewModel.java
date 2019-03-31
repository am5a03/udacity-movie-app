package com.raymondctc.udacity.popularmovies.ui.detail;

import com.raymondctc.udacity.popularmovies.data.repository.MovieDataSourceFactory;
import com.raymondctc.udacity.popularmovies.data.repository.MovieReviewDataSourceFactory;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

public class MovieReviewListViewModel extends ViewModel {

    @Inject
    MovieReviewDataSourceFactory movieDataSourceFactory;

    @Inject
    public MovieReviewListViewModel() {

    }

}
