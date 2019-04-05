package com.raymondctc.udacity.popularmovies.models.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;

public class ApiVideoResponse {
    public int id;
    public List<ApiVideo> results;

    public static class ApiVideo implements Parcelable {
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

        protected ApiVideo(Parcel in) {
            id = in.readString();
            iso6391 = in.readString();
            iso31661 = in.readString();
            key = in.readString();
            name = in.readString();
            site = in.readString();
            size = in.readInt();
            type = in.readString();
        }

        public static final Creator<ApiVideo> CREATOR = new Creator<ApiVideo>() {
            @Override
            public ApiVideo createFromParcel(Parcel in) {
                return new ApiVideo(in);
            }

            @Override
            public ApiVideo[] newArray(int size) {
                return new ApiVideo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(iso6391);
            dest.writeString(iso31661);
            dest.writeString(key);
            dest.writeString(name);
            dest.writeString(site);
            dest.writeInt(size);
            dest.writeString(type);
        }
    }
}
