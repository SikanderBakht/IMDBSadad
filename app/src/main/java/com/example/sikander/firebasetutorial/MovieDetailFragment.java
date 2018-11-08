package com.example.sikander.firebasetutorial;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailFragment extends Fragment {
    private TextView movieName;
    private TextView movieYear;
    private TextView movieRating;
    private TextView movieDescription;
    MovieListItem movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie = new MovieListItem();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieName = (TextView) view.findViewById(R.id.movie_name);
        movieYear = (TextView) view.findViewById(R.id.movie_year);
        movieRating = (TextView) view.findViewById(R.id.movie_rating);
        movieDescription = (TextView) view.findViewById(R.id.movie_description);

        int movieId = getArguments().getInt("movie_id");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="https://api.themoviedb.org/3/movie/" + movieId + "?api_key=a779580e00d1cae522d941e0aa841f69&language=en-US";
        final String imageBaseUrl = "http://image.tmdb.org/t/p/w185/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movie.setOverview(response.optString("overview"));
                        movie.setPosterPath(imageBaseUrl + response.optString("poster_path"));
                        movie.setVoteCount(response.optInt("vote_count"));
                        movie.setTitle(response.optString("title"));
                        movie.setBackdropPath(imageBaseUrl + response.optString("backdrop_path"));
                        movie.setPopularity(response.optLong("popularity"));
                        movie.setReleaseDate(response.optString("release_date"));
                        movieName.setText(movie.getTitle());
                        movieYear.setText(movie.getReleaseDate());
                        movieRating.setText(Long.toString(movie.getPopularity()));
                        movieDescription.setText(movie.getOverview());
                        Toast.makeText(getActivity(),"got data", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(getActivity(),"got data", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
        return view;
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
