package com.raymondctc.udacity.popularmovies.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.utils.image.Util;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerAppCompatActivity;

public class MovieDetailActivity extends DaggerAppCompatActivity {

    public static final String KEY_MOVIE_DETAIL = "movie_detail";
    public static final String KEY_MOVIE_POSITION = "movie_position";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MovieReviewListViewModel movieReviewListViewModel;
    private MovieReviewListPagedListAdapter pagedListAdapter;
    private RecyclerView recyclerView;

    private ApiMovie apiMovie;
    private int moviePositionInMainActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieReviewListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieReviewListViewModel.class);
        apiMovie = getIntent().getParcelableExtra(KEY_MOVIE_DETAIL);

        final ImageView imageView = findViewById(R.id.movie_thumbnail);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(Util.formatImagePath(apiMovie.posterPath)).into(imageView);
        collapsingToolbarLayout.setTitle(" "); // It cannot show multiple lines....

        moviePositionInMainActivity = getIntent().getIntExtra(KEY_MOVIE_POSITION, 0);
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
            movieReviewListViewModel.getVideos(apiMovie.id);
        });

        movieReviewListViewModel.getVideoListLiveData().observe(this, apiVideos -> {
            pagedListAdapter.addVideos(apiVideos);
        });

        movieReviewListViewModel.getVideoUriLiveData().observe(this, videoUri -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (i.resolveActivity(getPackageManager()) != null) {
                i.setData(videoUri);
                startActivity(i);
            }
        });

        getLifecycle().addObserver(movieReviewListViewModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(KEY_MOVIE_DETAIL, apiMovie);
        intent.putExtra(KEY_MOVIE_POSITION, moviePositionInMainActivity);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(movieReviewListViewModel);
    }
}
