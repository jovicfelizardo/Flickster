package com.codepath.claudia.flickster.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.claudia.flickster.R;
import com.codepath.claudia.flickster.models.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    /*take this view, pass to a view holder and put it onto the layout manager*/

    Context context;
    List<Movie> movies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("smile", "onCreateViewHolder");
        //instantiates a layout XML file into the corresponding view object
        //.from() obtains the layout inflator from that given context
        //.inflate() inflates a new view hierarchy from the XML node specified
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Log.d("smile", "onBindViewHolder: " + position);
        //getting a specific movie from the list stored in this adapter that takes a list of movies and makes views for them
        Movie movie = movies.get(position);
        //Bind the movie data into the view holder
        holder.bind(movie);

//        holder.itemView.setTag(new Integer(position));
        holder.ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clickable", "You clicked the image at position: "+holder.getAdapterPosition());

                //Create and show alert with movie review
                AlertDialog.Builder builder  = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);

                Log.d("hi", "The context is: " + context);

                builder.setCancelable(true);
                builder.setTitle("REVIEW");
                builder.setMessage(movies.get(holder.getAdapterPosition()).getReview() + "\n"
                        + "Rating: " + movies.get(holder.getAdapterPosition()).getVoteAverage() + "/10");

                builder.setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            //Reference the backdrop path if phone is in landscape
            String imgUrl = movie.getPosterPath();
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                imgUrl = movie.getBackdropPath();
            Glide.with(context).load(imgUrl).into(ivPoster);
        }
    }
}
