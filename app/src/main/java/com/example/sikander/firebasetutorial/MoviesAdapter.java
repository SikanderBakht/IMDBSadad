package com.example.sikander.firebasetutorial;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private Context context;
    private ArrayList<MovieListItem> moviesList;

    public MoviesAdapter(Context context, ArrayList<MovieListItem> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_list_row, viewGroup, false);
        return new MoviesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesViewHolder moviesViewHolder, int position) {
        MovieListItem movie = moviesList.get(position);
        GlideApp.with(context).load(movie.getPosterPath()).placeholder(R.drawable.ic_launcher_background).into(moviesViewHolder.poster);
        moviesViewHolder.title.setText(movie.getTitle());
        moviesViewHolder.description.setText(movie.getOverview());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;
        TextView title, description;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("movie_id", moviesList.get(getAdapterPosition()).getId());
                    intent.putExtra("movie_poster", moviesList.get(getAdapterPosition()).getBackdropPath());
                    context.startActivity(intent);
                }
            });
        }
    }
}
