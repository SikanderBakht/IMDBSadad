package com.example.sikander.firebasetutorial;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sikander.firebasetutorial.Models.MovieReview;

public class MovieReviewViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView bodyView;
    public MovieReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.review_heading_view);
        bodyView = itemView.findViewById(R.id.review_description_view);
    }
    public void bindToPost(MovieReview movieReview) {
        titleView.setText(movieReview.reviewheadline);
        bodyView.setText(movieReview.reviewtext);
    }
}
