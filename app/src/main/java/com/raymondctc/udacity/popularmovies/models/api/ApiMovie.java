package com.raymondctc.udacity.popularmovies.models.api;

import com.squareup.moshi.Json;

import java.util.List;

public class ApiMovie {
    @Json(name = "poster_path")
    String posterPath;

    boolean adult;

    String overview;

    @Json(name = "release_date")
    String releaseDate;

    @Json(name = "genre_ids")
    List<Integer> genreIds;

    int id;

    @Json(name = "original_title")
    String originalTitle;

    @Json(name = "original_language")
    String originalLanguage;

    String title;

    @Json(name = "backdrop_path")
    String backdropPath;

    String popularity;

    @Json(name = "vote_count")
    int voteCount;

    boolean video;

    @Json(name = "vote_average")
    float voteAverage;
}
