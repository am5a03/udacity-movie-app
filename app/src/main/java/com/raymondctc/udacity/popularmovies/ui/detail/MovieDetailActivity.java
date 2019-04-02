package com.raymondctc.udacity.popularmovies.ui.detail;

import android.os.Bundle;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerAppCompatActivity;

public class MovieDetailActivity extends DaggerAppCompatActivity {

    public static final String KEY_MOVIE_DETAIL = "movie_detail";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    MovieReviewListViewModel movieReviewListViewModel;

    private MovieReviewListPagedListAdapter pagedListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        final ApiMovie apiMovie = getIntent().getParcelableExtra(KEY_MOVIE_DETAIL);
        pagedListAdapter = new MovieReviewListPagedListAdapter(apiMovie);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pagedListAdapter);

        movieReviewListViewModel.getPagedLiveData(apiMovie.id).observe(this, apiReviews -> {
            pagedListAdapter.submitList(apiReviews);
        });
    }
}
