package com.example.sikander.firebasetutorial;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnDeleteUser,btnLogout;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;
    ProgressBar progressBar;
    private ArrayList<MovieListItem> mostPopularMoviesList;
    private ArrayList<MovieListItem> trendingMoviesList;
    private ArrayList<MovieListItem> newestMoviesList;
    private ArrayList<MovieListItem> highestRatedMoviesList;
    MoviesListFragment moviesListFragment;
    Bundle extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDeleteUser =(Button) findViewById(R.id.delete_account);
        btnLogout =(Button) findViewById(R.id.logout_button);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mostPopularMoviesList = trendingMoviesList = newestMoviesList = highestRatedMoviesList = new ArrayList<MovieListItem>();
        moviesListFragment = new MoviesListFragment();
        extra = new Bundle();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user  = firebaseAuth.getCurrentUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        getNewestMovies();
        getTrendingMoviesList();
        getMostPopularMovies();
        getHighestRatedMovies();
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
    public void getTrendingMoviesList() {
        progressBar.setVisibility(View.VISIBLE);
        String url = BuildConfig.BASE_URL + "/3/trending/movie/week?api_key=" + BuildConfig.TMDB_API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        trendingMoviesList = Utils.parseJsonArray(response.optJSONArray("results").toString(), new TypeToken<List<MovieListItem>>() {
                        }.getType());
                        extra.putParcelableArrayList("trending_movies_list", trendingMoviesList);
                        moviesListFragment.setArguments(extra);
                        if(extra.size() > 3) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.sample_content_fragment, moviesListFragment);
                            transaction.commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRequest.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void getHighestRatedMovies() {
        progressBar.setVisibility(View.VISIBLE);
        String url = BuildConfig.BASE_URL + "/3/discover/movie?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US&sort_by=vote_average.desc&include_adult=false&include_video=false&page=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        highestRatedMoviesList = Utils.parseJsonArray(response.optJSONArray("results").toString(), new TypeToken<List<MovieListItem>>() {
                        }.getType());
                        extra.putParcelableArrayList("highest_rated_movies_list", highestRatedMoviesList);
                        moviesListFragment.setArguments(extra);
                        if(extra.size() > 3) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.sample_content_fragment, moviesListFragment);
                            transaction.commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRequest.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void getNewestMovies() {
        progressBar.setVisibility(View.VISIBLE);
        String url = BuildConfig.BASE_URL + "3/discover/movie?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US&sort_by=release_date.desc&include_adult=false&include_video=false&page=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        newestMoviesList = Utils.parseJsonArray(response.optJSONArray("results").toString(), new TypeToken<List<MovieListItem>>() {
                        }.getType());
                        extra.putParcelableArrayList("newest_movies_list", newestMoviesList);
                        moviesListFragment.setArguments(extra);
                        if(extra.size() > 3) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.sample_content_fragment, moviesListFragment);
                            transaction.commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRequest.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void getMostPopularMovies() {
        progressBar.setVisibility(View.VISIBLE);
        String url =BuildConfig.BASE_URL + "3/discover/movie?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        mostPopularMoviesList = Utils.parseJsonArray(response.optJSONArray("results").toString(), new TypeToken<List<MovieListItem>>() {
                        }.getType());
                        extra.putParcelableArrayList("most_popular_movies_list", mostPopularMoviesList);
                        moviesListFragment.setArguments(extra);
                        if(extra.size() > 3) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.sample_content_fragment, moviesListFragment);
                            transaction.commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRequest.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            invalidateOptionsMenu();
        } else if(item.getItemId() == R.id.sign_in) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem logoutItem = menu.findItem(R.id.logout);
        MenuItem loginItem = menu.findItem(R.id.sign_in);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            logoutItem.setVisible(true);
            loginItem.setVisible(false);
        } else {
            logoutItem.setVisible(false);
            loginItem.setVisible(true);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(10000);
        searchView.setIconifiedByDefault(true);
        return true;
    }
}
