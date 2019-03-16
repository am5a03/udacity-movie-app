package com.raymondctc.udacity.popularmovies.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.data.MovieDataSource;

import javax.inject.Inject;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;


public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    MovieListViewModel movieListViewModel;

    private MoviePagedListAdapter pagedListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(movieListViewModel::refresh);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Timber.d("layoutManager=" + position);
                if (R.layout.view_layout_progress == pagedListAdapter.getItemViewType(position)) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        pagedListAdapter = new MoviePagedListAdapter(getApplicationContext());
        recyclerView.setAdapter(pagedListAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieListViewModel.class);
        movieListViewModel.getPagedLiveData()
                .observe(this, apiMovie -> {
                    Timber.d("@@ onCreate: " + apiMovie);
                    pagedListAdapter.submitList(apiMovie);
                    swipeRefreshLayout.setRefreshing(false);
                });
        movieListViewModel.getListState()
                .observe(this, state -> pagedListAdapter.setNetworkState(state));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort:
                ChangeSortingDialogFragment fragment = ChangeSortingDialogFragment.newInstance();
                fragment.setListener(new ChangeSortingDialogFragment.Listener() {
                    @Override
                    public void onChooseTopRating() {
                        Timber.d("onChooseTopRating");
                        swipeRefreshLayout.setRefreshing(true);
                        movieListViewModel.refreshByType(MovieDataSource.TYPE_BY_RATING);
                    }

                    @Override
                    public void onChoosePopularity() {
                        Timber.d("onChoosePopularity");
                        swipeRefreshLayout.setRefreshing(true);
                        movieListViewModel.refreshByType(MovieDataSource.TYPE_BY_POPULARITY);
                    }
                });
                fragment.show(getSupportFragmentManager(), "");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }
}
