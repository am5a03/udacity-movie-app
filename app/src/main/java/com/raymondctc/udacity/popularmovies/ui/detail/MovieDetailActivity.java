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
    private RecyclerView recyclerView;
    private AdapterObserver adapterObserver = new AdapterObserver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        final ApiMovie apiMovie = getIntent().getParcelableExtra(KEY_MOVIE_DETAIL);
        pagedListAdapter = new MovieReviewListPagedListAdapter(apiMovie);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pagedListAdapter);

        pagedListAdapter.registerAdapterDataObserver(adapterObserver);

        movieReviewListViewModel.getPagedLiveData(apiMovie.id).observe(this, apiReviews -> {
            pagedListAdapter.submitList(apiReviews);
        });
    }

    @Override
    protected void onDestroy() {
        pagedListAdapter.unregisterAdapterDataObserver(adapterObserver);
        super.onDestroy();
    }

    private class AdapterObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            if (positionStart == 0) {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
        }
    }
}
