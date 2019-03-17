package com.raymondctc.udacity.popularmovies.models.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ApiMovie implements Parcelable {
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

    protected ApiMovie(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readString();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readFloat();
    }

    public static final Creator<ApiMovie> CREATOR = new Creator<ApiMovie>() {
        @Override
        public ApiMovie createFromParcel(Parcel in) {
            return new ApiMovie(in);
        }

        @Override
        public ApiMovie[] newArray(int size) {
            return new ApiMovie[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "title={" + title + "}," + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeFloat(voteAverage);
    }
}
