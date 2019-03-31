package com.raymondctc.udacity.popularmovies.models.api;

import com.squareup.moshi.Json;

public class ApiReviewResponse {
    public int id;

    public int page;

    @Json(name = "total_pages")
    public int totalPages;

    @Json(name = "total_results")
    public int totalResults;

    public static class ApiReview {
        public int id;
        public String author;
        public String content;
    }
}
