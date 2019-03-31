package com.raymondctc.udacity.popularmovies.models.api;

import com.squareup.moshi.Json;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

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

        public static DiffUtil.ItemCallback<ApiReview> DIFF_CALLBACK = new DiffUtil.ItemCallback<ApiReview>() {
            @Override
            public boolean areItemsTheSame(@NonNull ApiReview oldItem, @NonNull ApiReview newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull ApiReview oldItem, @NonNull ApiReview newItem) {
                return oldItem.equals(newItem);
            }
        };
    }
}
