package com.example.sikander.firebasetutorial.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sikander.firebasetutorial.Activities.LoginActivity;
import com.example.sikander.firebasetutorial.Activities.SubmitReviewActivity;
import com.example.sikander.firebasetutorial.Activities.VideoPlayerActivity;
import com.example.sikander.firebasetutorial.Activities.ViewAllReviewsActivity;
import com.example.sikander.firebasetutorial.BuildConfig;
import com.example.sikander.firebasetutorial.GlideApp;
import com.example.sikander.firebasetutorial.Models.MovieListItem;
import com.example.sikander.firebasetutorial.Models.MovieReview;
import com.example.sikander.firebasetutorial.MovieTrailer;
import com.example.sikander.firebasetutorial.R;
import com.example.sikander.firebasetutorial.Utils;
import com.example.sikander.firebasetutorial.VolleyRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.sikander.firebasetutorial.Activities.MovieDetailActivity.BASE_BACKDROP_PATH;

public class MovieDetailFragment extends Fragment {
    private TextView movieName;
    private TextView movieYear;
    private TextView movieRating;
    private TextView movieDescription;
    private TextView reviewHeading;
    private TextView reviewDescription;
    private ImageView moviePoster;
    private CardView userReviewsCard;
    private MovieListItem movie;
    private Button addReviewBtn;
    private Button seeAllReviewBtn;
    private LinearLayout trailersLinearLayout;
    private HorizontalScrollView trailersScrollView;
    private DatabaseReference mDatabase;
    MovieReview movieReview;
    private long movieId;
    private ArrayList<MovieTrailer> movieTrailers;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        movie = new MovieListItem();
        movieReview = new MovieReview();
        movieTrailers = new ArrayList<MovieTrailer>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieName = (TextView) view.findViewById(R.id.movie_name);
        movieYear = (TextView) view.findViewById(R.id.movie_year);
        movieRating = (TextView) view.findViewById(R.id.movie_rating);
        movieDescription = (TextView) view.findViewById(R.id.movie_description);
        reviewHeading = (TextView) view.findViewById(R.id.review_heading_view);
        reviewDescription = (TextView) view.findViewById(R.id.review_description_view);
        moviePoster = (ImageView) view.findViewById(R.id.poster);
        userReviewsCard = (CardView) view.findViewById(R.id.user_reviews_card);
        addReviewBtn = (Button) view.findViewById(R.id.add_review);
        seeAllReviewBtn = (Button) view.findViewById(R.id.see_all_reviews_btn);
        trailersLinearLayout = (LinearLayout) view.findViewById(R.id.trailers);
        trailersScrollView = (HorizontalScrollView) view.findViewById(R.id.trailers_container);
        movieId = getArguments().getLong("movie_id");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="https://api.themoviedb.org/3/movie/" + movieId + "?api_key=a779580e00d1cae522d941e0aa841f69&language=en-US";
        final String imageBaseUrl = "http://image.tmdb.org/t/p/w185/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movie = Utils.parseJson(response.toString(), MovieListItem.class);
                        movieName.setText(movie.getTitle());
                        movieYear.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
                        movieRating.setText(String.format(getString(R.string.rating), String.valueOf(movie.getVote_average())));
                        movieDescription.setText(movie.getOverview());
                        GlideApp.with(getActivity()).load(BASE_BACKDROP_PATH + movie.getPoster_path()).into(moviePoster);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
        getTrailers();
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent(getActivity(), SubmitReviewActivity.class);
                    intent.putExtra("movie_details", getArguments());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("movie_details", getArguments());
                    startActivity(intent);
                }
            }
        });
        seeAllReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewAllReviewsActivity.class);
                intent.putExtra("movie_id", movieId);
                startActivity(intent);
            }
        });
    }
    public void getTrailers() {
        String url = BuildConfig.BASE_URL + "/3/movie/" + movieId + "/videos?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressBar.setVisibility(View.GONE);
                        movieTrailers = Utils.parseJsonArray(response.optJSONArray("results").toString(), new TypeToken<List<MovieTrailer>>() {
                        }.getType());
                        showTrailers(movieTrailers);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRequest.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
    public void showTrailers(ArrayList<MovieTrailer> trailers)
    {
        if (trailers.isEmpty())
        {
            this.trailersLinearLayout.setVisibility(View.GONE);
            trailersScrollView.setVisibility(View.GONE);

        } else
        {
            this.trailersLinearLayout.setVisibility(View.VISIBLE);
            trailersScrollView.setVisibility(View.VISIBLE);
            this.trailersLinearLayout.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPrimary)
                    .centerCrop()
                    .override(150, 150);

            for (final MovieTrailer trailer : trailers)
            {
                View thumbContainer = inflater.inflate(R.layout.trailer, this.trailersLinearLayout, false);
                ImageView thumbView = thumbContainer.findViewById(R.id.video_thumb);
                thumbView.setTag(R.id.glide_tag, MovieTrailer.getUrl(trailer));
                thumbView.requestLayout();
                thumbView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String videoUrl = (String) v.getTag(R.id.glide_tag);
                        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                        intent.putExtra("trailer_details", trailer);
                        startActivity(intent);
                    }
                });
                Glide.with(getContext())
                        .load(MovieTrailer.getThumbnailUrl(trailer))
                        .apply(options)
                        .into(thumbView);
                this.trailersLinearLayout.addView(thumbContainer);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference ref = mDatabase.child("movie-reviews");
        Query phoneQuery = ref.child(String.valueOf(movieId)).limitToFirst(1);
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userReviewsCard.setVisibility(View.VISIBLE);
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    movieReview = singleSnapshot.getValue(MovieReview.class);
                    reviewHeading.setText(movieReview.reviewheadline);
                    reviewDescription.setText(movieReview.reviewtext);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
