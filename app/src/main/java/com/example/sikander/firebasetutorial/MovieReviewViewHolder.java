package com.example.sikander.firebasetutorial;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieReviewViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    /*public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;*/
    public TextView bodyView;
    public MovieReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.review_heading_view);
        /*authorView = itemView.findViewById(R.id.postAuthor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.postNumStars);*/
        bodyView = itemView.findViewById(R.id.review_description_view);
    }
    public void bindToPost(MovieReview movieReview/*, View.OnClickListener starClickListener*/) {
        titleView.setText(movieReview.reviewheadline);
        /*authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));*/
        bodyView.setText(movieReview.reviewtext);
        //starView.setOnClickListener(starClickListener);
    }
}
