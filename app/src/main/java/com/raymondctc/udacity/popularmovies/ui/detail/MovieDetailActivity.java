package com.raymondctc.udacity.popularmovies.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.utils.image.Util;

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
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        final ApiMovie apiMovie = getIntent().getParcelableExtra(KEY_MOVIE_DETAIL);
        pagedListAdapter = new MovieReviewListPagedListAdapter(apiMovie, movieReviewListViewModel.getClickListener());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pagedListAdapter);

        movieReviewListViewModel.getPagedLiveData(apiMovie.id).observe(this, apiReviews -> {
            pagedListAdapter.submitList(apiReviews);
            if (recyclerView != null && recyclerView.getLayoutManager() != null) {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }

            // Workaround to avoid incorrect index in adapter,
            // fetch trailers after fetching comments
            movieReviewListViewModel.getDisposable().add(
                    movieReviewListViewModel.getVideos(apiMovie.id)
                    .subscribe(apiVideoResponse -> {
                        pagedListAdapter.addVideos(apiVideoResponse.results);
                    })
            );
        });

        movieReviewListViewModel.getVideoUriLiveData().observe(this, videoUri -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(videoUri);
            startActivity(i);
        });

        getLifecycle().addObserver(movieReviewListViewModel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        movieReviewListViewModel.saveInstaceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(movieReviewListViewModel);
    }
}
