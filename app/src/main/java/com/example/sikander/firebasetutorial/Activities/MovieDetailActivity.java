package com.example.sikander.firebasetutorial.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.sikander.firebasetutorial.GlideApp;
import com.example.sikander.firebasetutorial.Fragments.MovieDetailFragment;
import com.example.sikander.firebasetutorial.R;
import com.google.firebase.auth.FirebaseAuth;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w780";
    private ImageView movieImage;
    FrameLayout movieDetailsContent;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(extras);
        movieImage = findViewById(R.id.htab_header);
        movieDetailsContent = findViewById(R.id.movie_detail);
        String movieBackdrop = getIntent().getExtras().getString("movie_poster");
        String movieTitle = getIntent().getExtras().getString("movie_title");
        GlideApp.with(MovieDetailActivity.this).load(BASE_BACKDROP_PATH + movieBackdrop).into(movieImage);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(movieTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#51000000")));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.movie_detail, movieDetailFragment);
        ft.commit();
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
