package com.example.aidan.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.Models.Steps;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.example.aidan.bakingapp.Adapters.StepsListAdapter.SELECTED_POSITION_EXTRA;
import static com.example.aidan.bakingapp.Adapters.StepsListAdapter.STEPS_EXTRA;

@SuppressWarnings("StringBufferReplaceableByString")
public class VideoPlayerActivity extends AppCompatActivity {
    public static final String PLAYER_USER_AGENT = "bakeStepVideo";
    private static final String PLAYER_POSITION = "playerPosition";
    private static long currentPlayerPosition = -1;
    @BindView(R.id.tv_step_desc)
    TextView tvStepDesc;
    @BindView(R.id.tv_step_number)
    TextView tvStepNo;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_prev)
    ImageView ivPrevious;
    @BindView(R.id.exo_no_video)
    ImageView ivNoVideoPlaceholder;
    @BindView(R.id.exo_buffering)
    ProgressBar pbBuffering;
    @BindView(R.id.vw_step_video)
    PlayerView pvStepVideo;
    @BindView(R.id.exo_shutter)
    View exoShutter;
    ExoPlayer mExoPlayer = null;
    List<Steps> stepsList;
    Bakes bakes;
    int totalSteps = 0;
    int stepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            currentPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
        }
        if (intent.hasExtra(STEPS_EXTRA)) {
            bakes = intent.getParcelableExtra(STEPS_EXTRA);
            stepsList = bakes.getStepsList();
            stepPosition = intent.getIntExtra(SELECTED_POSITION_EXTRA, 0);
            totalSteps = (stepsList.size() - 1);
            getSelectedStep(stepsList.get(stepPosition));
        }
    }

    public void getSelectedStep(Steps steps) {
        String stepDesc;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(steps.getShortDescription());
        }
        if (steps.getVideoUrl().trim().equals("")) {
            pbBuffering.setVisibility(View.GONE);
            ivNoVideoPlaceholder.setVisibility(View.VISIBLE);
            exoShutter.setBackgroundColor(getResources().getColor(R.color.no_video_background));
        } else {
            ivNoVideoPlaceholder.setVisibility(View.INVISIBLE);
            exoShutter.setBackgroundColor(getResources().getColor(R.color.black_color));
            initializePlayer(Uri.parse(steps.getVideoUrl()));
        }
        tvStepDesc.setText(steps.getFullDescription());
        if (steps.getStepId() > 0) {
            stepDesc = new StringBuilder().append(getString(R.string.step)).append(" \t")
                    .append(steps.getStepId()).append(" / ").append(totalSteps).toString();
        } else {
            stepDesc = getString(R.string.introduction_step);
        }
        tvStepNo.setText(stepDesc);
    }

    public void initializePlayer(Uri sourceUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            pvStepVideo.setPlayer(mExoPlayer);
            mExoPlayer.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        pbBuffering.setVisibility(View.GONE);
                    } else if (playWhenReady) {
                        if (playbackState == Player.STATE_BUFFERING) {
                            pbBuffering.setVisibility(View.VISIBLE);
                            if (currentPlayerPosition != -1) {
                                mExoPlayer.seekTo(currentPlayerPosition);
                                currentPlayerPosition = -1;
                            }
                        }
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    super.onPlayerError(error);
                    Timber.d(error);
                }
            });
            String userAgent = Util.getUserAgent(this, PLAYER_USER_AGENT);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent)).createMediaSource(sourceUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @OnClick(R.id.iv_next)
    void nextStep() {
        int newPosition = stepPosition + 1;
        if (newPosition >= 0 && newPosition <= (stepsList.size() - 1)) {
            releasePlayer();
            pvStepVideo.setPlayer(null);
            stepPosition = newPosition;
            getSelectedStep(stepsList.get(stepPosition));
        }
    }

    @OnClick(R.id.iv_prev)
    void previousStep() {
        int newPosition = stepPosition - 1;
        if (newPosition >= 0 && newPosition <= (stepsList.size() - 1)) {
            releasePlayer();
            pvStepVideo.setPlayer(null);
            stepPosition = newPosition;
            getSelectedStep(stepsList.get(stepPosition));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null)
            outState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
