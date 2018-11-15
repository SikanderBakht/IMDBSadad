package com.example.sikander.firebasetutorial;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewAllReviewsActivity extends AppCompatActivity {
    public static final String EXTRA_REVIEW_KEY = "review_key";
    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ValueEventListener mReviewListener;
    private String mPostKey;
    private RecyclerView mRecyclerView;
    ProgressBar progressBar;
    FloatingActionButton newPostFloatBtn;
    private FirebaseRecyclerAdapter<MovieReview, MovieReviewViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private long movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_reviews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        newPostFloatBtn = (FloatingActionButton) findViewById(R.id.new_post_floating_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        movieId = getIntent().getExtras().getLong("movie_id");
        Query reviewsQuery = getQuery(mDatabase);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MovieReview>()
                .setQuery(reviewsQuery, MovieReview.class)
                .build();
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("User Reviews");
        mAdapter = new FirebaseRecyclerAdapter<MovieReview, MovieReviewViewHolder>(options) {
            @Override
            public MovieReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                progressBar.setVisibility(View.VISIBLE);
                return new MovieReviewViewHolder(inflater.inflate(R.layout.review_list_row, viewGroup, false));
            }
            @Override
            protected void onBindViewHolder(MovieReviewViewHolder viewHolder, int position, final MovieReview model) {
                /*final DatabaseReference postRef = getRef(position);

                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }*/

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                progressBar.setVisibility(View.GONE);
                viewHolder.bindToPost(model);/*, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });*/

                //mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        newPostFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllReviewsActivity.this, SubmitReviewActivity.class);
                intent.putExtra("movie_details", getIntent().getExtras());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("movie-reviews").child(String.valueOf(movieId));
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
