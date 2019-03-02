package com.raymondctc.udacity.popularmovies.models.api;

import com.squareup.moshi.Json;

import java.util.List;

public class ApiMovieResponse {
    int page;

    @Json(name = "total_results")
    int totalResults;

    @Json(name = "total_pages")
    int totalPages;

    List<ApiMovie> results;
}
