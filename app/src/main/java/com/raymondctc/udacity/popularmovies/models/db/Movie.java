package com.raymondctc.udacity.popularmovies.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "movie_id")
    public int movieId;

    @ColumnInfo(name = "original_title")
    public String originalTitle;

    @ColumnInfo(name = "release_date")
    public String releaseDate;

    @ColumnInfo(name = "overview")
    public String overview;

    @ColumnInfo(name = "poster_path")
    public String posterPath;

    @ColumnInfo(name = "vote_average")
    public float voteAverage;

    @ColumnInfo(name = "fav_timestamp")
    public long favTimestamp;
}
