package com.itvedant.recyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pranit on 09-04-2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private List<Movie> moviesList;

    public MoviesAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        holder.genre.setText(movie.getGenre());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            year = (TextView) itemView.findViewById(R.id.year);
            genre = (TextView) itemView.findViewById(R.id.genre);
        }
    }
}
