package com.raymondctc.udacity.popularmovies.models.api;

import com.squareup.moshi.Json;

import java.util.List;

public class ApiVideoResponse {
    public int id;
    public List<ApiVideo> results;

    public static class ApiVideo {
        public String id;

        @Json(name = "iso_639_1")
        public String iso6391;

        @Json(name = "iso_3166_1")
        public String iso31661;

        public String key;

        public String name;

        public String site;

        public int size;

        public String type;
    }
}
