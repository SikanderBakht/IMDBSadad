package com.example.sikander.firebasetutorial;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnDeleteUser,btnLogout;
    FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth.AuthStateListener  authStateListener;
    private ArrayList<MovieListItem> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDeleteUser =(Button) findViewById(R.id.delete_account);
        btnLogout =(Button) findViewById(R.id.logout_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        moviesList = new ArrayList<MovieListItem>();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user  = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.themoviedb.org/3/discover/movie?api_key=a779580e00d1cae522d941e0aa841f69&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
        final String imageBaseUrl = "http://image.tmdb.org/t/p/w185/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray moviesArray = response.optJSONArray("results");
                            for(int i = 0; i < moviesArray.length(); i++) {
                                MovieListItem movieListItem = new MovieListItem();
                                JSONObject jsonObject = (JSONObject) moviesArray.get(i);
                                movieListItem.setVoteCount(jsonObject.optInt("vote_count"));
                                movieListItem.setId(jsonObject.optInt("id"));
                                movieListItem.setTitle(jsonObject.optString("title"));
                                movieListItem.setPosterPath(imageBaseUrl + jsonObject.optString("poster_path"));
                                movieListItem.setBackdropPath(imageBaseUrl + jsonObject.optString("backdrop_path"));
                                movieListItem.setOverview(jsonObject.optString("overview"));
                                moviesList.add(movieListItem);
                            }
                            mAdapter = new MoviesAdapter(MainActivity.this, moviesList);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this,"got data", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(MainActivity.this,"got data", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"User deleted",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }

    private void doMySearch(String query) {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=a779580e00d1cae522d941e0aa841f69&language=en-US&query=" + query + "&page=1&include_adult=false";
        final String imageBaseUrl = "http://image.tmdb.org/t/p/w185/";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray moviesArray = response.optJSONArray("results");
                            for(int i = 0; i < moviesArray.length(); i++) {
                                MovieListItem movieListItem = new MovieListItem();
                                JSONObject jsonObject = (JSONObject) moviesArray.get(i);
                                movieListItem.setVoteCount(jsonObject.optInt("vote_count"));
                                movieListItem.setId(jsonObject.optInt("id"));
                                movieListItem.setTitle(jsonObject.optString("title"));
                                movieListItem.setPosterPath(imageBaseUrl + jsonObject.optString("poster_path"));
                                movieListItem.setBackdropPath(imageBaseUrl + jsonObject.optString("backdrop_path"));
                                movieListItem.setOverview(jsonObject.optString("overview"));
                                moviesList.add(movieListItem);
                            }
                            mAdapter = new MoviesAdapter(MainActivity.this, moviesList);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this,"got data", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(MainActivity.this,"got data", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        return true;
    }
}
