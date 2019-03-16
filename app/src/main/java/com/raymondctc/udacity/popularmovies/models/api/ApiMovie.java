package com.raymondctc.udacity.popularmovies.models.api;

import com.squareup.moshi.Json;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ApiMovie {
    @Json(name = "poster_path")
    public String posterPath;

    public boolean adult;

    public String overview;

    @Json(name = "release_date")
    public String releaseDate;

    @Json(name = "genre_ids")
    public List<Integer> genreIds;

    public int id;

    @Json(name = "original_title")
    public String originalTitle;

    @Json(name = "original_language")
    public String originalLanguage;

    public String title;

    @Json(name = "backdrop_path")
    public String backdropPath;

    public String popularity;

    @Json(name = "vote_count")
    public int voteCount;

    public boolean video;

    @Json(name = "vote_average")
    public float voteAverage;

    public static DiffUtil.ItemCallback<ApiMovie> DIFF_CALLBACK = new DiffUtil.ItemCallback<ApiMovie>() {
        @Override
        public boolean areItemsTheSame(@NonNull ApiMovie oldItem, @NonNull ApiMovie newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ApiMovie oldItem, @NonNull ApiMovie newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "title={" + title + "}," + super.toString();
    }
}
