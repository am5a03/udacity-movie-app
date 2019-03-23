package com.raymondctc.udacity.popularmovies.models.api;

import com.squareup.moshi.Json;

import java.util.List;

public class ApiMovieResponse {
    public int page;

    @Json(name = "total_results")
    public int totalResults;

    @Json(name = "total_pages")
    public int totalPages;

    public List<ApiMovie> results;
}
