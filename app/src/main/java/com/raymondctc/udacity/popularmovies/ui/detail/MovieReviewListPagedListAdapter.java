package com.raymondctc.udacity.popularmovies.ui.detail;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.raymondctc.udacity.popularmovies.models.api.ApiReviewResponse;
import com.raymondctc.udacity.popularmovies.models.api.ApiVideoResponse;
import com.raymondctc.udacity.popularmovies.utils.image.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MovieReviewListPagedListAdapter extends PagedListAdapter<ApiReviewResponse.ApiReview, RecyclerView.ViewHolder> {

    private final ApiMovie apiMovie;
    private final View.OnClickListener clickListener;
    private final List<ApiVideoResponse.ApiVideo> trailers = new ArrayList<>();

    protected MovieReviewListPagedListAdapter(ApiMovie apiMovie, View.OnClickListener clickListener) {
        super(ApiReviewResponse.ApiReview.DIFF_CALLBACK);
        this.apiMovie = apiMovie;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == R.layout.view_movie_detail) {
            MovieDetailViewHolder viewHolder = new MovieDetailViewHolder(view);
            viewHolder.favButton.setOnClickListener(this.clickListener);
            return viewHolder;
        }

        if (viewType == R.layout.view_trailer) {
            TrailerViewHolder viewHolder = new TrailerViewHolder(view);
            viewHolder.playButton.setOnClickListener(this.clickListener);
            return viewHolder;
        }

        if (viewType == R.layout.view_review) {
            return new ReviewViewHolder(view);
        }

        throw new RuntimeException("Unknown view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieDetailViewHolder) {
            final MovieDetailViewHolder movieDetailViewHolder = (MovieDetailViewHolder) holder;
            Picasso.get().load(Util.formatImagePath(apiMovie.posterPath))
                    .into(movieDetailViewHolder.thumbnail);

            movieDetailViewHolder.title.setText(apiMovie.originalTitle);
            movieDetailViewHolder.overview.setText(apiMovie.overview);
            movieDetailViewHolder.year.setText(apiMovie.releaseDate);
            movieDetailViewHolder.rating.setText(String.valueOf(apiMovie.voteAverage + "/10"));
            movieDetailViewHolder.favButton.setTag(apiMovie);
        }

        if (holder instanceof TrailerViewHolder) {
            ApiVideoResponse.ApiVideo video = trailers.get(position - 1);
            final TrailerViewHolder viewHolder = (TrailerViewHolder) holder;
            viewHolder.trailerTitle.setText("Trailer " + position);
            viewHolder.playButton.setTag(video);
        }

        if (holder instanceof ReviewViewHolder) {
            ApiReviewResponse.ApiReview review = getItem( position - trailers.size() - 1);
            final ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
            reviewViewHolder.author.setText(review.author);
            reviewViewHolder.reviews.setText(Html.fromHtml(review.content));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.view_movie_detail;
        }

        if (trailers.size() > 0 && position <= trailers.size()) {
            return R.layout.view_trailer;
        }

        return R.layout.view_review;
    }

    @Override
    public int getItemCount() {
        return  1 + // Movie detail
                trailers.size() + // Num of trailers
                super.getItemCount() // Reviews
                ;
    }

    public void addVideos(List<ApiVideoResponse.ApiVideo> videos) {
        trailers.addAll(videos);
        notifyItemRangeInserted(1, videos.size());
    }

    public static class MovieDetailViewHolder extends RecyclerView.ViewHolder {

        public final ImageView thumbnail;
        public final TextView title;
        public final TextView overview;
        public final TextView year;
        public final TextView rating;
        public final TextView length;
        public final CheckBox favButton;

        public MovieDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.movie_thumbnail);
            title = itemView.findViewById(R.id.title);
            overview = itemView.findViewById(R.id.overview);
            year = itemView.findViewById(R.id.year);
            rating = itemView.findViewById(R.id.rating);
            length = itemView.findViewById(R.id.length);
            favButton = itemView.findViewById(R.id.fav);
        }
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {

        public final TextView trailerTitle;
        public final Button playButton;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.trailerTitle = itemView.findViewById(R.id.title);
            this.playButton = itemView.findViewById(R.id.play);
        }
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public final TextView author;
        public final TextView reviews;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            reviews = itemView.findViewById(R.id.reviews);
        }
    }
}
