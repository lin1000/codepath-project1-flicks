package com.codepath.week1assignment;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by lin1000 on 2017/2/19.
 */

public class QuickPlayActivity extends YouTubeBaseActivity {

    @BindView(R.id.youtubeplayer) YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube);
        ButterKnife.bind(this);

        log.d(this.getClass().getName(),"youtubeview");

        final String videoCode = getIntent().getStringExtra("video_code");
        log.d(this.getClass().getName(), videoCode);


        youTubePlayerView.initialize("AIzaSyD3Hcjmj-OvGtgEBYBJEYRn2MuWc7rVagk",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.
                        youTubePlayer.setShowFullscreenButton(false);
                        //youTubePlayer.cueVideo(videoCode);
                        youTubePlayer.loadVideo(videoCode);

                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
