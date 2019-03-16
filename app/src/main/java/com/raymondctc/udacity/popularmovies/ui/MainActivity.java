package com.raymondctc.udacity.popularmovies.ui;

import android.os.Bundle;
import android.util.Log;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;


public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    MovieListViewModel movieListViewModel;

    private MoviePagedListAdapter pagedListAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        pagedListAdapter = new MoviePagedListAdapter(getApplicationContext());
        recyclerView.setAdapter(pagedListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        movieListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieListViewModel.class);
        movieListViewModel.getPagedLiveData()
                .observe(this, apiMovie -> {
                    Timber.d("@@ onCreate: " + apiMovie);
                    pagedListAdapter.submitList(apiMovie);
                });

    }
}
