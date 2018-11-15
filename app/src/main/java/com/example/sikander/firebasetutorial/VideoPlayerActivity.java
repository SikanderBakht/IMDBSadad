package com.example.sikander.firebasetutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView youTubeView;
    private TextView trailerTitile;
    private MovieTrailer trailer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        trailer = getIntent().getExtras().getParcelable("trailer_details");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        trailerTitile = (TextView) findViewById(R.id.trailer_title);
        youTubeView.initialize(Api.API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        trailerTitile.setText(trailer.getName());
        youTubePlayer.loadVideo(trailer.getKey());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
