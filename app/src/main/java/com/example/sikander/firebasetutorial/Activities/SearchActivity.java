package com.example.sikander.firebasetutorial.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sikander.firebasetutorial.Models.MovieListItem;
import com.example.sikander.firebasetutorial.Adapters.MoviesAdapter;
import com.example.sikander.firebasetutorial.R;
import com.example.sikander.firebasetutorial.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ProgressBar progressBar;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MovieListItem> searchList;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchList = new ArrayList<MovieListItem>();
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
    private void doMySearch(String query) {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=a779580e00d1cae522d941e0aa841f69&language=en-US&query=" + query + "&page=1&include_adult=false";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        searchList = Utils.parseJsonArray(response.optJSONArray("results").toString(), new TypeToken<List<MovieListItem>>() {
                        }.getType());
                        mAdapter = new MoviesAdapter(SearchActivity.this, searchList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
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
        return true;
    }
}
