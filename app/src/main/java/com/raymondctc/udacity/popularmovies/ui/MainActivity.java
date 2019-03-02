package com.raymondctc.udacity.popularmovies.ui;

import android.os.Bundle;

import com.raymondctc.udacity.popularmovies.R;

import androidx.annotation.Nullable;
import dagger.android.support.DaggerAppCompatActivity;


public class MainActivity extends DaggerAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
