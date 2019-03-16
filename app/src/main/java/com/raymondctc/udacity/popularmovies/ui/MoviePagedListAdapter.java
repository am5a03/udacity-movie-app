package com.raymondctc.udacity.popularmovies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raymondctc.udacity.popularmovies.R;
import com.raymondctc.udacity.popularmovies.data.MovieDataSource;
import com.raymondctc.udacity.popularmovies.models.api.ApiMovie;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class MoviePagedListAdapter extends PagedListAdapter<ApiMovie, RecyclerView.ViewHolder> {

    private Context context;
    private int networkState;

    protected MoviePagedListAdapter(Context context) {
        super(ApiMovie.DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch (viewType) {
            case R.layout.view_layout_progress:
                return new ProgressViewHolder(v);
            default:
                return new MovieViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            final ApiMovie apiMovie = getItem(position);
            final String imagePath = "https://image.tmdb.org/t/p/w185/" + apiMovie.posterPath;
            Picasso.get()
                    .load(imagePath)
                    .into(((MovieViewHolder) holder).thumbnail)
            ;
            ((MovieViewHolder) holder).title.setText(apiMovie.title);
            Timber.d("@@ posterPath=" + imagePath);
        } else if (holder instanceof ProgressViewHolder){

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.view_layout_progress;
        }
        return R.layout.view_layout_movie;
    }

    public boolean hasExtraRow() {
        if (this.networkState == MovieDataSource.ListState.LOADING) {
            return true;
        } else {
            return false;
        }
    }

    public void setNetworkState(int networkState) {
        Timber.d("networkstate=" + networkState);
        int prevState = this.networkState;

        if (prevState == networkState) return;

        boolean hadExtraRow = hasExtraRow();
        this.networkState = networkState;
        boolean hasExtraRow = hasExtraRow();



        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && prevState != networkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        final ImageView thumbnail;
        final TextView title;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
            thumbnail = itemView.findViewById(R.id.movie_thumbnail);
        }
    }
}
