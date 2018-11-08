package com.example.sikander.firebasetutorial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView movieImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        String moviePoster = getIntent().getExtras().getString("movie_poster");

        movieImage = findViewById(R.id.htab_header);
        GlideApp.with(MovieDetailActivity.this).load(moviePoster).into(movieImage);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) getSupportActionBar().setTitle("IMDB");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle extras = getIntent().getExtras();
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(extras);
        adapter.addFrag(movieDetailFragment, "Movie Detail");
        viewPager.setAdapter(adapter);
    }
}
