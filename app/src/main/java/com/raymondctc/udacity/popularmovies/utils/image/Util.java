package com.raymondctc.udacity.popularmovies.utils.image;

public final class Util {
    private Util() {}

    public static String formatImagePath(String path) {
        return "https://image.tmdb.org/t/p/w185/" + path;
    }

    public static String formatYoutubeVideoPath(String youtubeVideoId) {
        return "https://www.youtube.com/watch?v=" + youtubeVideoId;
    }
}
