package com.itvedant.realmdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itvedant.realmdemo.R;
import com.itvedant.realmdemo.models.Movie;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Pranit on 30-08-2016.
 */

public class MovieAdapter extends RealmRecyclerViewAdapter<Movie, MovieAdapter.MovieHolder> {
    private Context context;

    public MovieAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Movie> data) {
        super(context, data, true);
        this.context = context;
    }

    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieHolder holder, int position) {
        Movie movie = getData().get(position);

        holder.title.setText(movie.getName());
        holder.desc.setText(movie.getDescription());
    }

    public class MovieHolder extends RecyclerView.ViewHolder{
        public TextView title, desc;

        public MovieHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
