package com.example.sikander.firebasetutorial.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sikander.firebasetutorial.Models.MovieReview;
import com.example.sikander.firebasetutorial.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class SubmitReviewActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private EditText reviewHeadline;
    private EditText reviewText;
    private Button reviewSubmitBtn;
    private Bundle extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_review);
        reviewHeadline = findViewById(R.id.review_headline);
        reviewText = findViewById(R.id.review_text);
        reviewSubmitBtn = findViewById(R.id.review_submit_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = firebaseAuth.getInstance();
        extra = getIntent().getExtras().getBundle("movie_details");
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(extra.getString("movie_title"));
        reviewSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMovieReview(firebaseAuth.getUid(), extra.getLong("movie_id"), reviewHeadline.getText().toString(), reviewText.getText().toString());
                Toast.makeText(SubmitReviewActivity.this, "Review Submitted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void writeMovieReview(String userId, Long movieId, String reviewHeadline, String reviewText) {
        String key = mDatabase.child("reviews").push().getKey();
        MovieReview movieReview = new MovieReview(userId, movieId, reviewHeadline, reviewText);
        Map<String, Object> reviewValues = movieReview.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/reviews/" + key, reviewValues);
        childUpdates.put("/movie-reviews/" + movieId + "/" + key, reviewValues);
        mDatabase.updateChildren(childUpdates);
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
